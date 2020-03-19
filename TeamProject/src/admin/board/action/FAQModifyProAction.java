package admin.board.action;

import java.sql.Timestamp;
import java.util.Spliterator;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.oreilly.servlet.MultipartRequest;
import com.oreilly.servlet.multipart.DefaultFileRenamePolicy;

import action.Action;
import admin.board.svc.FAQModifyProService;
import vo.ActionForward;
import vo.BoardBean;
import vo.FileBean;

public class FAQModifyProAction implements Action {

	@Override
	public ActionForward execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		ActionForward forward = null;
		
		
		// 멀티파트 정보 가져오기
		// 글 수정을 위해 필요한 정보만 가져오기 글번호, 제목, 내용
		// MultipartRequest 객체생성
		String saveFolder ="/boardFile";
				
		ServletContext context = request.getServletContext(); // request 객체로부터 컨텍스트 객체 가져오기
		String realFolder = context.getRealPath(saveFolder); // 가상 위치로부터 실제 폴더 위치를 가져오기
				
		int maxSize=10*1024*1024; // 1024 비트(1키로바이트) * 1024 키로바이트(1메가바이트) * 10 => 10 메가바이트 
		MultipartRequest multi=new MultipartRequest(request,realFolder,maxSize,"utf-8",new DefaultFileRenamePolicy());
		
		String originFilename = multi.getOriginalFileName((String)multi.getFileNames().nextElement());
		String storedFileName = multi.getFilesystemName((String)multi.getFileNames().nextElement());
		
		String[] getFileType = originFilename.split(".");
		String fileType = getFileType[getFileType.length - 1];
		
		System.out.println("원본 파일명(보여지는 이름) : " + originFilename);
		System.out.println("저장된 파일명(중복처리) : " + storedFileName);
		
		// 카테고리 관련
		int boardNum = Integer.parseInt(multi.getParameter("boardNum"));
		String k1 = multi.getParameter("k1");
				
		// 제목과 내용, 작성자
		String boardTitle = multi.getParameter("boardTitle");
		String boardContent = multi.getParameter("boardContent");
		
		FileBean file = new  FileBean(originFilename, storedFileName, fileType);
		// BoardBean 에 파라미터 저장 및 생성
		BoardBean bb = new BoardBean(boardNum, k1, boardTitle, boardContent, file);
		
		FAQModifyProService faq_ModifyProService = new FAQModifyProService();
		
		// 성공여부는 리턴받음
		boolean isModifySuccess = false;
		
		isModifySuccess = faq_ModifyProService.modifyArticle(bb);
		
		forward = new ActionForward();
		forward.setPath("./FAQDetail.adb?num"+boardNum);
		forward.setRedirect(true);
		
		return forward;
	}

}
