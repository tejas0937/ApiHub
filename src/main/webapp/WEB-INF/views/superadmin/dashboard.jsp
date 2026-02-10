<%@ page contentType="text/html;charset=UTF-8" %>
<!DOCTYPE html>
<html>
<head>
<title>Super Admin Dashboard</title>

<style>
*{margin:0;padding:0;box-sizing:border-box;font-family:"Segoe UI",Arial;}
body{background:#000;color:#fff;}

.navbar{padding:22px 60px;display:flex;justify-content:space-between;border-bottom:1px solid #222;}
.logo{font-size:26px;font-weight:700;}
.page{padding:60px;}
.card{border:1px solid #222;border-radius:10px;padding:24px;max-width:420px;}
</style>
</head>

<body>

<header class="navbar">
 <div class="logo">APIHUB</div>
 <div>
  <a style="color:white;text-decoration:none"
     href="<%=request.getContextPath()%>/">Home</a>
 </div>
</header>

<section class="page">

 <h2>Super Admin Dashboard</h2><br>

 <div class="card">
  <p>Global monitoring of organizations, platform admins, APIs and plans.</p>
  <br>
  <p>Modules will be connected dynamically later.</p>
 </div>

</section>

</body>
</html>
