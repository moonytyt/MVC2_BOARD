<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>
	 <c:if test="${msg=='실패' }">
	 	<script type="text/javascript">
	 		alert("비밀번호가 틀렸습니다.");
	 		history.go(-1);
	 	</script>
	 </c:if>
	 <c:if test="${msg=='수정성공' }">
	 	<script type="text/javascript">
	 		alert("게시글수정이 완료되었습니다");
	 	</script>
	 </c:if>
	  <c:if test="${msg=='삭제성공' }">
	 	<script type="text/javascript">
	 		alert("게시글을 삭제했습니다.");
	 	</script>
	 </c:if>
	<center>
		<h2>전체 게시글 보기</h2>
		<table width="700" border="1">
			<tr height="40">
				<td align="right" colspan="5">
					<button onclick="location.href='BoardWriteForm.jsp'">글쓰기</button>
				</td>
			</tr>
			<tr height="40">
				<td width="50" align="center">번호</td>
				<td width="320" align="center">제목</td>
				<td width="100" align="center">작성자</td>
				<td width="150" align="center">작성일</td>
				<td width="80" align="center">조회수</td>
			</tr>
			<c:set var="number" value="${number }" />
			<c:forEach var="list" items="${list }">
				<tr height="40">
					<td width="50" align="left">${number }</td>
					<td width="320" align="left">
						<c:if test="${list.re_step>1 }">
						<c:forEach var="j" begin="1" end="${(list.re_step-1)*5}">
							&nbsp;
						</c:forEach>
						</c:if>
						<a href="BoardInfoControl.do?num=${list.num }">${list.subject }</a>
					</td>
					<td width="100" align="left">${list.writer }</td>
					<td width="150" align="left">${list.reg_date }</td>
					<td width="80" align="left">${list.readcount }</td>
				</tr>
				<c:set var="number" value="${number - 1 }"/>
			</c:forEach>
		</table>
		<p>
		
		<!-- 페이지 카운터링 소스를 작성 -->
		<c:if test="${count>0 }">
			<c:set var="pageCount" value="${count/pageSize +(count%pageSize == 0 ? 0: 1) }" />
			<c:set var="startPage" value="${1 }" />
			<c:if test="${currentPage%10!=0 }">
				<!--결과를 정수형으로 리턴 받아야 하기에 fmt  -->
				<fmt:parseNumber var="result" value="${currentPage/10 }" integerOnly="true" />
				<c:set var="startPage" value="${result*10+1 }"/>
			</c:if>
			<c:if test="${currentPage%10==0 }">
				<!--결과를 정수형으로 리턴 받아야 하기에 fmt  -->
				<c:set var="startPage" value="${(result-1)*10+1 }"/>
			</c:if>
			
			<!-- 화면에 보여질 페이지 처리 숫자를 표현 -->
			<c:set var="pageBlock" value="${10 }" />
			<c:set var="endPage" value="${startPage + pageBlock-1 }" />
			<c:if test="${endPage> pageCount }">
				<c:set var="endPage" value="${pageCount }" />
			</c:if>
			<!-- 이전 링크를 걸지 파악  -->
			<c:if test="${startPage >10 }">
				<a href="BoardListCon.do?pageNum=${startPage-10 }">[이전] </a>
			</c:if>
			<!-- 페이징처리 -->
			<c:forEach var="i" begin="${startPage }" end="${endPage }">
				<a href="BoardListCon.do?pageNum=${i}">[${i}]</a>
			</c:forEach>
			<!-- 다음 -->
			<c:if test="${endPage<pageCount }">
				<a href="BoardListCon.do?pageNum=${startPage+10 }">[다음] </a>
			</c:if>			
		</c:if>		
	</center>

</body>
</html>