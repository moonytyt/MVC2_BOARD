<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title></title>
</head>
<body>
<center>
	<h2> 게시글 수정</h2>
	<form action="BoardDeleteProcCon.do" method ="post">
		<table width = "600" border = "1">
			
			<tr height= "40">
				<td width="120" align="center">패스워드</td>
				<td width="480" colspan="3">&nbsp;<input type="password" name="password" size="60"></td>
			</tr>
			<tr height= "40">
				<td colspan="4" align="center">
					<input type="hidden" name="num" value="${bean.num }">
					<input type="hidden" name="ref" value="${bean.ref}">
					<input type="hidden" name="pass" value="${bean.password }">
					<input type="submit" value="글삭제">&nbsp;&nbsp;
					<input type="button" onclick = "location.href='BoardListCon.do'" value="전체 글 보기">
				</td>
			</tr>
		</table>
	</form>
</center>
</body>
</html>