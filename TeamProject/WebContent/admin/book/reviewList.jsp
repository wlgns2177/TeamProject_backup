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
��ǰ �ı� ����
<ul>
    <li><a href="ReviewDetail.abook">1�� ��ǰ</a>
        <input type="button" value="�亯�ϱ�" onclick="location.href='ReviewWriteForm.abook'">
        <input type="button" value="�����ϱ�" onclick="location.href='ReviewModifyForm.abook'"></li>
    <li>2�� ��ǰ</li>
    <li>3�� ��ǰ</li>
    <li>4�� ��ǰ</li>
</ul>
<input type="button" value="�����ϱ�" onclick="location.href='ReviewDeleteForm.abook'">
</body>
</html>