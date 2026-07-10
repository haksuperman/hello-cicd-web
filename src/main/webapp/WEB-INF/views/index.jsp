<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <title>hello-cicd-web</title>
    <style>
        body {
            font-family: system-ui, -apple-system, sans-serif;
            background: #ffffff;
            color: #1a1a1a;
            margin: 48px auto;
            max-width: 640px;
            padding: 0 16px;
        }
        table {
            border-collapse: collapse;
            margin-top: 24px;
        }
        th, td {
            border: 1px solid #ddd;
            padding: 8px 16px;
            text-align: left;
        }
        th {
            background: #f6f6f6;
            font-weight: 600;
        }
    </style>
</head>
<body>
    <h1>${msg}</h1>
    <p>오늘 날짜: ${today}</p>
    <table>
        <tr>
            <th>버전</th>
            <td>${version}</td>
        </tr>
        <tr>
            <th>빌드 시각</th>
            <td>${buildTimestamp}</td>
        </tr>
    </table>
</body>
</html>
