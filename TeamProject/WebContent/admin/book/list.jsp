<%@ page language="java" contentType="text/html; charset=EUC-KR"
    pageEncoding="EUC-KR"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="EUC-KR">
<title>Insert title here</title>
</head>
<body>
<a href="./admin/adminMain.jsp">��������</a>
��ǰ ����<br>
<input type="text"><input type="button" value="��ǰ�˻�" onclick="location.href='Search.abook'">
<ul>
    <li><a href="Detail.abook">1�� ��ǰ</a><input type="button" value="�����ϱ�" onclick="location.href='ModifyForm.abook'"></li>
    <li>2�� ��ǰ</li>
    <li>3�� ��ǰ</li>
    <li>4�� ��ǰ</li>
</ul>
<input type="button" value="�����ϱ�" onclick="location.href='DeleteForm.abook'">
</body>
</html>