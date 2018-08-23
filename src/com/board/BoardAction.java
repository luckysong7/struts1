package com.board;

import java.net.URLDecoder;
import java.net.URLEncoder;
import java.sql.Connection;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionRedirect;
import org.apache.struts.actions.DispatchAction;

import com.util.DBCPConn;
import com.util.MyUtil;

public class BoardAction extends DispatchAction {

	public ActionForward write(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		return mapping.findForward("created");
	
	}
	
	public ActionForward write_ok(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		Connection conn = DBCPConn.getConnection(); // DBCP DB 按眉 积己
		BoardDAO dao = new BoardDAO(conn); // DAO 按眉 积己
		
		BoardForm f = (BoardForm)form; // 促款某胶泼
		
		f.setNum(dao.getMaxNum()+1);
		f.setIpAddr(request.getRemoteAddr());
		
		dao.insertData(f);
	
		return mapping.findForward("save");
	
	}
	
	public ActionForward list(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		BoardDAO dao = new BoardDAO(DBCPConn.getConnection());
		String cp = request.getContextPath();
		
		MyUtil myUtil = new MyUtil();
		int numPerPage = 3;
		int totalPage = 0 ; 
		int totalDataCount = 0;
		
		String pageNum = request.getParameter("pageNum");
		
		int currentPage = 1;
		if(pageNum != null){
			currentPage = Integer.parseInt(pageNum);
			
		}
		String searchKey = request.getParameter("searchKey");
		String searchValue = request.getParameter("searchValue");
		
		if(searchKey == null){
			searchKey = "subject";
			searchValue = "";
			
		}
		if(request.getMethod().equalsIgnoreCase("GET")){
			searchValue = URLDecoder.decode(searchValue, "UTF-8");
			
		}
		
		totalDataCount = dao.getDataCount(searchKey, searchValue);
		
		if(totalDataCount != 0){
			totalPage = myUtil.getPageCount(numPerPage, totalDataCount);
					
		}
		
		if(currentPage > totalPage){
			currentPage = totalPage;
			
		}
		
		int start = (currentPage-1)*numPerPage+1;
		int end = (currentPage) * numPerPage;
		
		List<BoardForm> lists = dao.getLists(start, end, searchKey, searchValue);
		
		String param = "";
		String urlArticle = "";
		String urlList = "";
		
		if(!searchValue.equals("")){
			searchValue = URLEncoder.encode(searchValue,"UTF-8");
			param = "&searchKey="+searchKey;
			param += "&searchValue="+searchValue;
		}
		
		urlList = cp + "/board.do?method=list"+param;
		urlArticle = cp+"/board.do?method=article&pageNum="+currentPage;
		urlArticle += param;
		
		request.setAttribute("lists", lists);
		request.setAttribute("urlArticle", urlArticle);
		request.setAttribute("pageNum", pageNum);
		request.setAttribute("pageIndexList",
				myUtil.pageIndexList(currentPage, totalPage, urlList));
		request.setAttribute("totalPage", totalPage);
		request.setAttribute("totalDataCount", totalDataCount);
		
		return mapping.findForward("list");
	
	}
	
	public ActionForward article(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		BoardDAO dao = new BoardDAO(DBCPConn.getConnection());
		String cp = request.getContextPath();
		
		int num = Integer.parseInt(request.getParameter("num"));
		String pageNum = request.getParameter("pageNum");
		
		String searchKey = request.getParameter("searchKey");
		String searchValue = request.getParameter("searchValue");
		
		BoardForm dto = dao.getReadData(num);
		if(searchKey != null){
			searchValue = URLDecoder.decode(searchValue, "UTF-8");	
		}
		
		String urlList = "";
		
		if(dto == null){
			urlList = cp + "/board.do?method=list";
			response.sendRedirect("url");
		}
		int lineSu = dto.getContent().split("\n").length;
		
		dto.setContent(dto.getContent().replaceAll("\n", "<br/>"));
		
		String param = "pageNum="+pageNum;
		if(searchKey != null){
			param += "&searchKey=" + searchKey;
			param += "&searchValue=" + URLEncoder.encode(searchValue, "UTF-8"); 
		}
		
		request.setAttribute("dto", dto);
		
		request.setAttribute("params", param);
		
		request.setAttribute("lineSu", lineSu);
		
		request.setAttribute("pageNum", pageNum);
		
		//炼雀荐 刘啊
		dao.updateHitCount(num);
		
		return mapping.findForward("article");
	
	}
	
	public ActionForward update(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		Connection conn = DBCPConn.getConnection(); // DBCP DB 按眉 积己
		BoardDAO dao = new BoardDAO(conn); // DAO 按眉 积己
		String cp = request.getContextPath();
		
		int num = Integer.parseInt(request.getParameter("num"));
		String pageNum = request.getParameter("pageNum");

		String searchKey = request.getParameter("searchKey");
		String searchValue = request.getParameter("searchValue");
		
		if(searchKey != null){
			searchValue = URLDecoder.decode(searchValue, "UTF-8");
			
		}
		
		BoardForm dto = dao.getReadData(num);
		
		String urlList= "";
		
		if(dto == null){
			urlList = cp + "/board.do?method=list";
			response.sendRedirect("url");
		}
		String param = "pageNum="+pageNum;
		
		if(searchKey != null){
			param += "&searchKey=" + searchKey;
			param += "&searchValue=" + URLEncoder.encode(searchValue, "UTF-8"); 
		}
		
		request.setAttribute("dto", dto);
		
		request.setAttribute("pageNum", pageNum);

		request.setAttribute("params", param);
		
		request.setAttribute("searchKey", searchKey);
		
		request.setAttribute("searchValue", searchValue);
		
		return mapping.findForward("update");
		
	}
	
	
	public ActionForward update_ok(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		Connection conn = DBCPConn.getConnection(); // DBCP DB 按眉 积己
		BoardDAO dao = new BoardDAO(conn); // DAO 按眉 积己
		String cp = request.getContextPath();
		
		String pageNum = request.getParameter("pageNum");
		String searchKey = request.getParameter("searchKey");
		String searchValue = request.getParameter("searchValue");
		
		BoardForm dto = new BoardForm();
		
		dto.setNum(Integer.parseInt(request.getParameter("num")));
		dto.setSubject(request.getParameter("subject"));
		dto.setName(request.getParameter("name"));
		dto.setEmail(request.getParameter("email"));
		dto.setContent(request.getParameter("content"));
		dto.setPwd(request.getParameter("pwd"));
					
		dao.updateData(dto);
		
		String params = "&pageNum="+pageNum;
		
		if(searchKey != null){
			params += "&searchKey=" + searchKey;
			params += "&searchValue=" + URLEncoder.encode(searchValue, "UTF-8"); 
		}
		
		String urlList = "";
		
		urlList = cp+"/board.do?method=list&"+params;
	
		ActionRedirect redirect = new ActionRedirect(mapping.findForward("update_ok"));
		redirect.addParameter("params",params);
		return redirect;
	}
	
	public ActionForward delete_ok(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		Connection conn = DBCPConn.getConnection(); // DBCP DB 按眉 积己
		BoardDAO dao = new BoardDAO(conn); // DAO 按眉 积己
		String cp = request.getContextPath();
		
		int num = Integer.parseInt(request.getParameter("num"));
		
		String pageNum = request.getParameter("pageNum");
		String searchKey = request.getParameter("searchKey");
		String searchValue = request.getParameter("searchValue");
		
		
		dao.deleteData(num);
		
		return mapping.findForward("delete");
	}
}
