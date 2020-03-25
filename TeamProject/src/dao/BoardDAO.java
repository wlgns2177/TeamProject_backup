package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import vo.BoardBean;
import vo.FileBean;

import static db.JdbcUtil.*;

public class BoardDAO {
	public BoardDAO() {}
	
	private static BoardDAO instance = new BoardDAO();
	
	public static BoardDAO getInstance() {
		return instance;
	}
	
	private Connection con = null;
	private PreparedStatement pstmt = null;
	private ResultSet rs = null;

	public void setConnection(Connection con) {
		this.con = con;
	}
	////////////////////////////////////////////////////////////////////////////

	// ************카테고리 번호(kID) 를 가져오는 메서드 get_kId
		public int get_kID(String k1, String k2) {
			int kID = -1;
			
			String sql = "SELECT kID FROM kategorie Where k1=? and k2=?";
			try {
				pstmt = con.prepareStatement(sql);
				pstmt.setString(1, k1);
				pstmt.setString(2, k2);
				
				rs = pstmt.executeQuery();
				
				if(rs.next()) {
					kID = rs.getInt(1);
				}
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				close(rs); close(pstmt);
			}
			
			return kID;
		}
	// ***************해당 대분류(k1)에서 가장 큰 글 번호를 가져오는 메서드 getMaxNum
	public int getMaxNum(String k1) {
		int maxNum = 0;
		
		String sql = "SELECT MAX(boardNum) FROM board WHERE kID IN (SELECT kID FROM kategorie WHERE k1=?)";
		try {
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, k1);
			
			rs = pstmt.executeQuery();
			
			if(rs.next()) {
				maxNum = rs.getInt(1);
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			close(rs); close(pstmt);
		}
		
		return maxNum;
	}
	//
	//
	//
	//
	//  글 작성
	
	public int insertArticle(BoardBean bb) {
		int insertCount = 0;
		
		int kID = get_kID(bb.getK1(), bb.getK2()); //  1:1 문의 답변 용으로 따로 체크해야함
		String sql = "INSERT INTO board values(boardNum, kID, boardWriter, boardTitle,"
				+ " boardContent, boardRegTime, boardReRef, boardReLev, boardReSeq, boardReadcount, bookID)";
		
		try {
			pstmt = con.prepareStatement(sql);
			pstmt.setInt(1, bb.getBoardNum()); pstmt.setInt(2, kID); pstmt.setString(3, bb.getBoardWriter());
			pstmt.setString(4, bb.getBoardTitle()); pstmt.setString(5, bb.getBoardContent());
			pstmt.setTimestamp(6, bb.getBoardRegTime()); pstmt.setInt(7, bb.getBoardReRef()); 
			pstmt.setInt(8, bb.getBoardReLev()); pstmt.setInt(9, bb.getBoardReSeq()); 
			pstmt.setInt(10, bb.getBoardReadcount()); pstmt.setInt(11, bb.getBookID());
			int update = pstmt.executeUpdate();
			if(update != 0) {
				if(bb.getFileList().size() != 0) {
					insertCount = insertFile(bb, kID);
				} else {
					insertCount = update;
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			close(pstmt);
		}
		return insertCount;
	}
	
	
	// 글 쓸때 파일을 같이 업로드 하는 insertFile 메서드
	public int insertFile(BoardBean  bb, int kID) {
		int insertCount = 0;
		int count = 0;
		int listSize = bb.getFileList().size();
		try {
			for(FileBean file : bb.getFileList()) {
				String sql = "INSERT INTO boardFile VALUES(null, ?, ?, ?, ?, ?, ?)";
				pstmt = con.prepareStatement(sql);
				pstmt.setString(1, file.getOriginFilename());
				pstmt.setString(2, file.getstoredFileName());
				pstmt.setString(3, file.getFileType());
				pstmt.setInt(4, bb.getBoardNum());
				pstmt.setInt(5, kID);
				pstmt.setString(6, bb.getBoardWriter());
			
				count += pstmt.executeUpdate();
			}
			if(count != listSize) {
				// DB에 업로드 되지 않은 파일이 있을 경우 insertCount는 0이다.
			} else {
				insertCount = 1; // DB에 모든 파일이 업로드 되었을 경우 1을 반환
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			close(pstmt);
		}
		return insertCount;
	}
	
	
	// 글작성
	//
	//
	//
	//
	//
	//
	//
	//
	//-------------------------------------------------------------------------------
	// 글 내용 보는 메서드들
	
	public BoardBean selectArticle(int boardNum, String k1, String k2) {
		BoardBean bb = null;
		int kID = get_kID(k1, k2);
		
		String sql = "SELECT * FROM board WHERE boardNum=? AND kID=?";
		try {
			pstmt = con.prepareStatement(sql);
			pstmt.setInt(1, boardNum); pstmt.setInt(2, kID);
			
			rs = pstmt.executeQuery();
			
			if(rs != null) {
				bb = new BoardBean(rs.getInt("boardNum"), rs.getString("k1"), rs.getString("k2"),
						rs.getString("boardWriter"), rs.getString("boardTitle"), rs.getString("boardContent"),
						rs.getTimestamp("boardRegTime"), rs.getInt("boardReRef"), rs.getInt("boardReLev"),
						rs.getInt("boardReSeq"), rs.getInt("boardReadcount"), rs.getInt("bookID"));
				bb.setFileList(selectFileList(boardNum, kID));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			close(rs); close(pstmt);
		}
		
		return bb;
	}
	// 파일 가져오기
	public List<FileBean> selectFileList(int boardNum, int kID) {
		
		List<FileBean> fileList = new ArrayList<FileBean>();
		FileBean file = null;
		
		String sql = "SELECT * FROM boardFile WHERE board_boardNum=? AND board_kID=?";
		
		try {
			pstmt = con.prepareStatement(sql);
			pstmt.setInt(1, boardNum);
			pstmt.setInt(2, kID);
			
			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				file = new  FileBean(rs.getInt("fileNum"), rs.getString("originFilename"),
						rs.getString("storedFileName"), rs.getString("fileType"),
						rs.getInt("board_boardNum"), rs.getInt("board_kID"),
						rs.getString("board_boardWriter"));
				fileList.add(file);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			close(rs); close(pstmt);
		}
		
		
		return fileList;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	public int get_kID(int boardNum, String k1) {
		int kID = -1;
		
		String sql = "SELECT b.kID FROM board b JOIN kategorie k ON b.kID=k.kID WHERE b.boardNum=? AND k.k1=?";
		try {
			pstmt = con.prepareStatement(sql);
			pstmt.setInt(1, boardNum); pstmt.setString(2, k1);
			rs = pstmt.executeQuery();
			
			if(rs.next()) {
				kID = rs.getInt("b.kID");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			close(rs); close(pstmt);
		}
		
		
		return kID;
	}
	
	// 조회수 증가 메서드
		public int increaseReadCount(int boardNum, int kID) {
			int updateCount = 0;
			
			String sql = "UPDATE board SET readCount=readCount+1 WHERE boardNum=? AND kID=?";
			
			try {
				pstmt = con.prepareStatement(sql);
				pstmt.setInt(1, boardNum); pstmt.setInt(2, kID);
				updateCount = pstmt.executeUpdate();
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				close(pstmt);
			}
			
			return updateCount;
		}
	
	
	

	
	
	
	//
	//
	//-------------------------------------------------------------------------------
	// 글 내용 보는 메서드들
	
	// 글 삭제하는 메서드들
	
		public int deleteArticle(int boardNum, String k1, String k2) {
			int deleteCount = 0;
			int kID = get_kID(k1, k2);
			
			List<FileBean> fileList = selectFileList(boardNum, kID);
			
			if(fileList.size() > 0) {
				int deleteFile = deleteAllFile(boardNum, kID);
				if(deleteFile == 0) {
					return 0;
				}
			}
			String sql = "DELETE FROM board WHERE boardNum=? AND kID=?";
			try {
				pstmt = con.prepareStatement(sql);
				pstmt.setInt(1, boardNum); pstmt.setInt(2, kID);
				
				int delete = pstmt.executeUpdate();
				if(delete != 0) {
					deleteCount = deleteFile(boardNum, kID);
				}
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				close(pstmt);
			}
			return deleteCount;
		}
	
		public int deleteAllFile(int boardNum, int kID) {
			int deleteFile = 0;
			
			String sql = "DELETE FROM boardFile WHERE board_boardNum=? AND board_kID=?";
			try {
				pstmt = con.prepareStatement(sql);
				pstmt.setInt(1, boardNum); pstmt.setInt(2, kID);
				
				deleteFile = pstmt.executeUpdate();
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				close(pstmt);
			}
			
			return deleteFile;
		}
	
	
	
	
	
	//
	//
	// 글 수정하는 메서드들

	public int updateArticle(BoardBean bb) {
		int updateCount = 0;
		
		int kID = get_kID(bb.getBoardNum(), bb.getK1());
		String sql = "UPDATE board SET boardTitle=?, boardContent=? WHERE boardNum=? AND kID=?";
		
		try {
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, bb.getBoardTitle()); pstmt.setString(2, bb.getBoardContent());
			pstmt.setInt(3, bb.getBoardNum()); pstmt.setInt(4, kID);
			
			updateCount = pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			close(pstmt);
		}
		
		return updateCount;
	}

	
	
	
	

	public int deleteFile(int boardNum, int kID) {
		int deleteCount = 0;
		
		String sql = "SELECT * FROM fileBoard WHERE board_boardNum=? AND board_kID=?";
		try {
			pstmt=con.prepareStatement(sql);
			pstmt.setInt(1, boardNum); pstmt.setInt(2, kID);
			rs = pstmt.executeQuery();
			if(rs.next()) {
				sql = "DELETE FROM fileBoard WHERE board_boardNum=? AND board_kID=?";
				pstmt = con.prepareStatement(sql);
				pstmt.setInt(1, boardNum); pstmt.setInt(2, kID);
				deleteCount = pstmt.executeUpdate();
			} else {
				deleteCount = 1;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			close(pstmt);
		}
		
		return deleteCount;
	}


	
	
	
}
