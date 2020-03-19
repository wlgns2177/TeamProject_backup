package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

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

	// 카테고리 번호(kID) 를 가져오는 메서드 get_kId
	public int get_kID(String k1, String k2, String k3) {
		int kID = -1;
		
		String sql = "SELECT kID FROM kategorie Where k1=? and k2=? and k3=?";
		try {
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, k1);
			pstmt.setString(2, k2);
			pstmt.setString(3, k3);
			
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
	
	// 해당 대분류(k1)에서 가장 큰 글 번호를 가져오는 메서드 getMaxNum
	public int getMaxNum(String k1) {
		int maxNum = 0;
		
		String sql = "SELECT boardNum FROM board WHERE kID IN(SELECT kID FROM kategorie WHERE k1=?)";
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
		
		int maxNum = getMaxNum(bb.getK1()) + 1;
		int kID = get_kID(bb.getK1(), bb.getK2(), bb.getK3()); //  1:1 문의 답변 용으로 따로 체크해야함
		String sql = "INSERT INTO board values(boardNum, kID, boardWriter, boardTitle, boardContent, now(), boardReRef, boardReLev, boardReSeq, boardReadcount, bookID)";
		
		try {
			pstmt = con.prepareStatement(sql);
			pstmt.setInt(1, maxNum); pstmt.setInt(2, kID); pstmt.setString(3, bb.getBoardWriter());
			pstmt.setString(4, bb.getBoardTitle()); pstmt.setString(5, bb.getBoardContent());
			pstmt.setInt(6, bb.getBoardReRef()); pstmt.setInt(7, 0); pstmt.setInt(8, 0); pstmt.setInt(9, 0);
			pstmt.setInt(10, bb.getBookID());
			
			if(bb.getFile() == null) {
				insertCount = pstmt.executeUpdate();
			} else {
				int update = pstmt.executeUpdate();
				if(update != 0) {
					bb.setBoardNum(maxNum);
					
					insertCount = insertFile(bb, kID);
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
		
		FileBean file = bb.getFile();
		String sql = "INSERT INTO boardFile VALUES(null, ?, ?, ?, ?, ?, ?)";
		try {
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, file.getOriginFilename());
			pstmt.setString(2, file.getstoredFileName());
			pstmt.setString(3, file.getFileType());
			pstmt.setInt(4, bb.getBoardNum());
			pstmt.setInt(5, kID);
			pstmt.setString(6, bb.getBoardWriter());
			
			insertCount = pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			close(pstmt);
		}
		
		return insertCount;
	}

	
	
	
	//
	//
	//-------------------------------------------------------------------------------
	// 글 내용 보는 메서드들
	
	public BoardBean selectArticle(int boardNum, String k1) {
		BoardBean bb = null;
		
		String sql = "SELECT * FROM board b JOIN kategorie k ON b.kID = k.kID WHERE b.boardNum=? AND k.k1=?";
		try {
			pstmt = con.prepareStatement(sql);
			pstmt.setInt(1, boardNum); pstmt.setString(2, k1);
			
			rs = pstmt.executeQuery();
			
			if(rs != null) {
				bb = new BoardBean(rs.getInt("boardNum"), rs.getString("k1"), rs.getString("k2"), rs.getString("k3"),
						rs.getString("boardWriter"), rs.getString("boardTitle"), rs.getString("boardContent"),
						rs.getTimestamp("boardRegTime"), rs.getInt("boardReRef"), rs.getInt("boardReLev"),
						rs.getInt("boardReSeq"), rs.getInt("boardReadcount"), rs.getInt("bookID"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			close(rs); close(pstmt);
		}
		
		return bb;
	}

	public FileBean selectFile(int boardNum, String k1) {
		
		FileBean file = null;
		
		String sql = "SELECT * FROM boardFile f JOIN kategorie k ON f.board_kID = k.kID WHERE f.board_boardNum=? AND f.board_kID=?";
		
		try {
			pstmt = con.prepareStatement(sql);
			pstmt.setInt(1, boardNum);
			pstmt.setString(2, k1);
			
			rs = pstmt.executeQuery();
			
			if(rs != null) {
				file = new  FileBean(rs.getInt("fileNum"), rs.getString("originFilename"),
						rs.getString("storedFileName"), rs.getString("fileType"),
						rs.getInt("board_boardNum"), rs.getInt("board_kID"),
						rs.getString("board_boardWriter"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			close(rs); close(pstmt);
		}
		
		
		return file;
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

	
	
	
	//
	//
	// 글 삭제하는 메서드들
	
	public int deleteArticle(int boardNum, String k1) {
		int deleteCount = 0;
		int kID = get_kID(boardNum, k1);
		
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
