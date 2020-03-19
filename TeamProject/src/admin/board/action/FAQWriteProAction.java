package admin.board.action;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.oreilly.servlet.MultipartRequest;
import com.oreilly.servlet.multipart.DefaultFileRenamePolicy;

import action.Action;
import admin.board.svc.FAQWriteProService;
import vo.ActionForward;
import vo.BoardBean;
import vo.FileBean;

public class FAQWriteProAction implements Action {

	@Override
	public ActionForward execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		ActionForward forward = null;
		
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
		String k1 = multi.getParameter("k1");
		String k2 = multi.getParameter("k2");
		String k3 = multi.getParameter("k3");
		
		// 제목과 내용, 작성자
		String boardTitle = multi.getParameter("boardTitle");
		String boardContent = multi.getParameter("boardContent");
		String boardWriter = multi.getParameter("boardWriter");
		
		FileBean file = new  FileBean(originFilename, storedFileName, fileType);
		// BoardBean 에 파라미터 저장 및 생성
		BoardBean bb = new BoardBean(k1, k2, k3, boardWriter, boardTitle, boardContent, file);
		
		// BoardBean 객체를 전달하여 서비스의 writeArticle() 메서드를 실행하여  DB에 글을 삽입하고, 성공 시 글 번호를 반환받는다, 실패시 0을 반환
		FAQWriteProService faq_WriteProService = new FAQWriteProService();
		int boardNum = faq_WriteProService.writeArticle(bb);
		
		
		
		forward = new ActionForward();
		// FAQ 작성한거 상세보기
		forward.setPath("./FAQDetail.adb?num"+boardNum);
		forward.setRedirect(true);
		
		return forward;
	}

}
