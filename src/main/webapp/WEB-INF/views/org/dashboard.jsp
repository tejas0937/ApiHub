<%@ page contentType="text/html;charset=UTF-8" %>
<!DOCTYPE html>
<html>
<head>
<title>Org Dashboard</title>

<style>
*{margin:0;padding:0;box-sizing:border-box;font-family:"Segoe UI",Arial;}
body{background:#000;color:#fff;}
.navbar{padding:22px 60px;display:flex;justify-content:space-between;border-bottom:1px solid #222;}
.logo{font-size:26px;font-weight:700;}
.links a{color:#fff;text-decoration:none;margin-left:30px;}
.page{padding:60px;}
.card{border:1px solid #222;padding:25px;border-radius:10px;max-width:400px;}
</style>
</head>

<body>

<header class="navbar">
 <div class="logo">APIHUB</div>
 <div class="links">
  <a href="<%=request.getContextPath()%>/org/add-employee-page">Add employee</a>
  <a href="<%=request.getContextPath()%>/">Home</a>
 </div>
</header>

<section class="page">
 <h2>Organization Dashboard</h2><br>

 <div class="card">
  <p>Manage employees of your organization.</p><br>
  <a style="color:white" href="<%=request.getContextPath()%>/org/add-employee-page">
   Go to Add Employee
  </a>
 </div>
</section>

</body>
</html>
