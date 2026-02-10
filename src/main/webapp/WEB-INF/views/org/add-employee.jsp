<%@ page contentType="text/html;charset=UTF-8" %>
<!DOCTYPE html>
<html>
<head>
<title>Add Employee</title>

<style>
*{margin:0;padding:0;box-sizing:border-box;font-family:"Segoe UI",Arial;}
body{background:#000;color:#fff;}

.navbar{padding:22px 60px;display:flex;justify-content:space-between;border-bottom:1px solid #222;}
.logo{font-size:26px;font-weight:700;}

.page{min-height:80vh;display:flex;justify-content:center;align-items:center;}
.card{border:1px solid #222;padding:30px;border-radius:10px;width:420px;}

input,select,button{
 width:100%;padding:12px;border-radius:6px;border:1px solid #333;
 background:transparent;color:#fff;margin-bottom:14px;
}
button{background:#fff;color:#000;border:none;font-weight:600;}
select option{color:black;}
</style>
</head>

<body>

<header class="navbar">
 <div class="logo">APIHUB</div>
</header>

<section class="page">
 <div class="card">

  <h3>Add Organization Employee</h3><br>

  <form method="post"
        action="<%=request.getContextPath()%>/org/add-employee">

   <input name="name" placeholder="Full name" required>
   <input type="email" name="email" placeholder="Email" required>
   <input type="password" name="password" placeholder="Password" required>

   <select name="role" required>
    <option value="">Select role</option>
    <option value="VIEWER">Viewer</option>
    <option value="FINANCE">Financer</option>
   </select>

   <button type="submit">Create employee</button>

  </form>

 </div>
</section>

</body>
</html>
