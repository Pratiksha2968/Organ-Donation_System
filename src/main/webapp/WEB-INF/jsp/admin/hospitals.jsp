<!DOCTYPE html>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Manage Hospitals - Organ Donation System</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css" rel="stylesheet">
    <style>
        :root { --primary-color: #e74c3c; --sidebar-width: 260px; }
        body { background: #f8f9fa; }
        .sidebar { position: fixed; left: 0; top: 0; width: var(--sidebar-width); height: 100vh; background: linear-gradient(180deg, #2c3e50 0%, #1a252f 100%); padding: 20px; color: white; z-index: 1000; }
        .sidebar .logo { text-align: center; padding: 20px 0; border-bottom: 1px solid rgba(255,255,255,0.1); margin-bottom: 20px; }
        .sidebar .logo i { font-size: 2.5rem; color: #e74c3c; }
        .sidebar .nav-link { color: rgba(255,255,255,0.7); padding: 12px 20px; border-radius: 10px; margin-bottom: 5px; }
        .sidebar .nav-link:hover, .sidebar .nav-link.active { background: rgba(231, 76, 60, 0.2); color: white; }
        .main-content { margin-left: var(--sidebar-width); padding: 30px; }
        .table-container { background: white; border-radius: 15px; padding: 25px; box-shadow: 0 5px 20px rgba(0,0,0,0.08); }
    </style>
</head>
<body>
    <div class="sidebar">
        <div class="logo"><i class="fas fa-heartbeat"></i><h5 class="mt-2">Organ Donation</h5><small>Admin Panel</small></div>
        <nav class="nav flex-column">
            <a class="nav-link" href="/admin/dashboard"><i class="fas fa-tachometer-alt"></i> Dashboard</a>
            <a class="nav-link" href="/admin/donors"><i class="fas fa-hand-holding-heart"></i> Donors</a>
            <a class="nav-link" href="/admin/recipients"><i class="fas fa-user-injured"></i> Recipients</a>
            <a class="nav-link active" href="/admin/hospitals"><i class="fas fa-hospital"></i> Hospitals</a>
            <a class="nav-link" href="/admin/matches"><i class="fas fa-link"></i> Matches</a>
            <a class="nav-link" href="/admin/analytics"><i class="fas fa-chart-bar"></i> Analytics</a>
            <a class="nav-link mt-auto" href="/logout"><i class="fas fa-sign-out-alt"></i> Logout</a>
        </nav>
    </div>
    <div class="main-content">
        <h2 class="mb-4">Manage Hospitals</h2>
        <div class="table-container">
            <table class="table table-hover">
                <thead><tr><th>ID</th><th>Hospital Name</th><th>City</th><th>Contact</th><th>Registration No</th><th>Status</th></tr></thead>
                <tbody>
                    <c:forEach items="${hospitals}" var="hospital">
                        <tr>
                            <td>${hospital.id}</td>
                            <td>${hospital.hospitalName}</td>
                            <td>${hospital.city}, ${hospital.state}</td>
                            <td>${hospital.contactNumber}</td>
                            <td>${hospital.registrationNumber}</td>
                            <td>
                                <c:choose>
                                    <c:when test="${hospital.isVerified}"><span class="badge bg-success">Verified</span></c:when>
                                    <c:otherwise><span class="badge bg-warning">Pending</span></c:otherwise>
                                </c:choose>
                            </td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
        </div>
    </div>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
