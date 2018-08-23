<%@page import="java.net.URLEncoder"%>
<%@page import="java.net.URLDecoder"%>

<%@page import="com.board.BoardDAO"%>
<%@page import="com.util.DBConn"%>
<%@page import="java.sql.Connection"%>

<%@page import="java.sql.Connection"%>
<%@ page contentType="text/html; charset=UTF-8"%>

<%
	request.setCharacterEncoding("utf-8");
	String cp = request.getContextPath();
	
	
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>게 시 판(Struts1)</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>

<link rel="stylesheet" href="<%=cp%>/board/css/style.css" type="text/css"/>
<link rel="stylesheet" href="<%=cp%>/board/css/created.css" type="text/css"/>

<script type="text/javascript" src="<%=cp%>/board/js/util.js"></script>
<script type="text/javascript">

    function sendIt() {
        var f = document.myForm;
        f.action = "<%=cp%>/board.do?method=update_ok";
        f.submit();
    }

</script>

</head>

<body>

<div id="bbs">
	<div id="bbs_title">
	게 시 판(Struts1)
	</div>

	<form name="myForm" method="post" action="">
	<div id="bbsCreated">
		<div class="bbsCreated_bottomLine">
			<dl>
				<dt>제&nbsp;&nbsp;&nbsp;&nbsp;목</dt>
				<dd>
				      <input type="text" name="subject" value ="${dto.subject }" size="74" maxlength="100"  class="boxTF" />
				</dd>
			</dl>
		</div>

		<div class="bbsCreated_bottomLine">
			<dl>
				<dt>작성자</dt>
				<dd>
				      <input type="text" name="name" value ="${dto.name }" size="35" maxlength="20" class="boxTF" />
				</dd>
			</dl>
		</div>

		<div class="bbsCreated_bottomLine">
			<dl>
				<dt>E-Mail</dt>
				<dd>
				      <input type="text" name="email" value ="${dto.email }" size="35" maxlength="50" class="boxTF" />
				</dd>
			</dl>
		</div>

		<div id="bbsCreated_content">
			<dl>
				<dt>내&nbsp;&nbsp;&nbsp;&nbsp;용</dt>
				<dd>
				      <textarea name="content" cols="63" rows="12" class="boxTA">
				      ${dto.content }
				      </textarea>
				</dd>
			</dl>
		</div>

		<div class="bbsCreated_noLine">
			<dl>
				<dt>패스워드</dt>
				<dd>
				      <input type="password" name="pwd" value ="${dto.pwd }" size="35" maxlength="7" class="boxTF" />&nbsp;(게시물 수정 및 삭제시 필요 !!!)
				</dd>
			</dl>
		</div>
	</div>

	<div id="bbsCreated_footer">
		<input type="hidden" name ="num" value ="${dto.num }" />
		<input type ="hidden" name ="pageNum" value ="${pageNum }" />
		<input type="hidden" name ="searchKey" value ="${searchKey }" />
		<input type="hidden" name ="searchValue" value ="${searchValue }" />	
			
        <input type="button" value=" 수정하기 " class="btn2" onclick="sendIt();"/>
       
        <input type="button" value=" 수정취소 " class="btn2" onclick="javascript:location.href='<%=cp%>/board.do?method=list&${params }';"/>
	</div>

    </form>
</div>

</body>

</html>