package com.boardTest;

import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;

import com.util.MyUtil;
import com.util.dao.CommonDAO;
import com.util.dao.CommonDAOImpl;

public class BoardAction extends DispatchAction{

	// �ۿø���
	public ActionForward created(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		return mapping.findForward("created");
		
		
	}
	
	// DB�� �Խù� ����
	public ActionForward created_ok(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		CommonDAO dao = CommonDAOImpl.getInstance();
		
		BoardForm f = (BoardForm)form;
		
		int maxNum = dao.getIntValue("boardTest.maxNum");
		
		f.setNum(maxNum+1);
		f.setIpAddr(request.getRemoteAddr());
		
		dao.insertData("boardTest.insertData", f);
		dao = null;
		
		return mapping.findForward("created_ok");
		
	}
	
	// �Խù� ����
	public ActionForward list(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		CommonDAO dao = CommonDAOImpl.getInstance();
		
		String cp = request.getContextPath();
		
		MyUtil myUtil = new MyUtil();
		
		int numPerPage = 5;
		int totalPage = 0;
		int totalDataCount = 0;
		
		String pageNum = request.getParameter("pageNum");
		
		int currentPage = 1 ;
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
		
		Map<String, Object> hMap = new HashMap<String, Object>();
		
		hMap.put("searchKey", searchKey);
		hMap.put("searchValue", searchValue);
		
		totalDataCount = dao.getIntValue("boardTest.dataCount", hMap);
		
		if(totalDataCount != 0){
			totalPage = myUtil.getPageCount(numPerPage, totalDataCount);
			
		}
		if(currentPage > totalPage){
			currentPage = totalPage;
		}
		
		int start = (currentPage -1) * numPerPage +1;
		int end = (currentPage) * numPerPage;
		
		hMap.put("start", start);
		hMap.put("end", end);
		
		List<Object> lists = dao.getListData("boardTest.listData", hMap);
		
		String param = "";
		String urlArticle = "";
		String urlList = "";
		
		if(!searchValue.equals("")){
			searchValue =URLEncoder.encode(searchValue, "UTF-8");
			param = "&searchKey="+searchKey;
			param += "&searchValue="+searchValue;
		}
		
		urlList = cp + "/boardTest.do?method=list" + param;
		urlArticle = cp + "/boardTest.do?method=article&pageNum="+currentPage;
		urlArticle += param;
		
		request.setAttribute("lists", lists);
		request.setAttribute("urlArticle", urlArticle);
		request.setAttribute("pageNum", currentPage);
		request.setAttribute("pageIndexList", myUtil.pageIndexList(currentPage, totalPage, urlList));
		request.setAttribute("totalPage", totalPage);
		request.setAttribute("totalDataCount", totalDataCount);
		
		return mapping.findForward("list");
		
	}
	
	//�Խù�
	public ActionForward article(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		CommonDAO dao = CommonDAOImpl.getInstance();
		
		int num = Integer.parseInt(request.getParameter("num"));
		
		String pageNum = request.getParameter("pageNum");
		
		String searchKey = request.getParameter("searchKey");
		String searchValue = request.getParameter("searchValue");
		
		if(searchKey == null){
			searchKey = "subject";
			searchValue = "";
		}
		
		if(request.getMethod().equalsIgnoreCase("GET")){
			searchValue=URLDecoder.decode(searchValue, "UTF-8");
		}
		dao.updateData("boardTest.hitCountUpdate", num);
		
		BoardForm dto = (BoardForm)dao.getReadData("boardTest.readData", num);
		
		if(dto == null){
			return mapping.findForward("list");
		}
		int lineSu = dto.getContent().split("\n").length;
		
		dto.setContent(dto.getContent().replaceAll("\r\n", "<br/>"));
		
		// ������ ������
		String preUrl = "";
		String nextUrl = "";
		
		String cp = request.getContextPath();
		
		Map<String, Object> hMap = new HashMap<String, Object>();
		
		hMap.put("searchKey",searchKey);
		hMap.put("searchValue",searchValue);
		hMap.put("num", num);
		
		String preSubject ="";
	
		BoardForm preDTO = (BoardForm)dao.getReadData("boardTest.preReadData", hMap);
		
		if(preDTO != null){
			preUrl = cp + "/boardTest.do?method=article&pageNum="+pageNum;
			preUrl += "&num=" + preDTO.getNum();
			preSubject = preDTO.getSubject();
			
		}
		
		String nextSubject ="";
		BoardForm nextDTO = (BoardForm)dao.getReadData("boardTest.nextReadData", hMap);
		
		if(nextDTO != null){
			nextUrl = cp + "/boardTest.do?method=article&pageNum="+pageNum;
			nextUrl += "&num=" + nextDTO.getNum();
			nextSubject = nextDTO.getSubject();
			
		}
		
		String urlList = cp + "/boardTest.do?method=list&pageNum="+pageNum;
		
		if(!searchValue.equals("")){
			searchValue = URLEncoder.encode(searchValue, "UTF-8");
			urlList += "&searchKey=" + searchKey;
			urlList += "&searchValue=" + searchValue;
			
			if(!preUrl.equals("")){
				preUrl +=  "&searchKey=" + searchKey + "&searchValue=" + searchValue;
			}
			if(!nextUrl.equals("")){
				nextUrl +=  "&searchKey=" + searchKey + "&searchValue=" + searchValue;
			}
				
		}
			// ������ �������� ����� ����
			
			String paramArticle = "num=" + num + "&pageNum=" + pageNum;
			
			request.setAttribute("dto", dto);
			request.setAttribute("preSubject", preSubject);
			request.setAttribute("preUrl", preUrl);
			request.setAttribute("nextSubject", nextSubject);
			request.setAttribute("nextUrl", nextUrl);
			request.setAttribute("lineSu", lineSu);
			request.setAttribute("paramArticle", paramArticle);
			request.setAttribute("urlList", urlList);
			
			return mapping.findForward("article");
			
		
	}
		
	
	
	// ����
	
	
	
	// ������ �Խù� DB�� ����
	
	
	// ����
	
}
