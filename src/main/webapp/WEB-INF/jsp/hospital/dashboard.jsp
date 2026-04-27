<!DOCTYPE html>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Hospital Dashboard - Organ Donation System</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css" rel="stylesheet">
    <style>
        :root { --primary-color: #9b59b6; --sidebar-width: 260px; }
        body { background: #f8f9fa; }
        .sidebar {
            position: fixed; left: 0; top: 0; width: var(--sidebar-width);
            height: 100vh; background: linear-gradient(180deg, #9b59b6 0%, #8e44ad 100%);
            padding: 20px; color: white; z-index: 1000;
        }
        .sidebar .logo { text-align: center; padding: 20px 0; border-bottom: 1px solid rgba(255,255,255,0.1); margin-bottom: 20px; }
        .sidebar .logo i { font-size: 2.5rem; }
        .sidebar .nav-link { color: rgba(255,255,255,0.8); padding: 12px 20px; border-radius: 10px; margin-bottom: 5px; }
        .sidebar .nav-link:hover, .sidebar .nav-link.active { background: rgba(255,255,255,0.2); color: white; }
        .main-content { margin-left: var(--sidebar-width); padding: 30px; }
        .stat-card { background: white; border-radius: 15px; padding: 25px; box-shadow: 0 5px 20px rgba(0,0,0,0.08); }
    </style>
</head>
<body>
    <div class="sidebar">
        <div class="logo">
            <i class="fas fa-hospital"></i>
            <h5 class="mt-2">Hospital Portal</h5>
        </div>
        <nav class="nav flex-column">
            <a class="nav-link active" href="/hospital/dashboard"><i class="fas fa-tachometer-alt"></i> Dashboard</a>
            <a class="nav-link" href="/hospital/profile"><i class="fas fa-user"></i> My Profile</a>
            <a class="nav-link" href="/hospital/matches"><i class="fas fa-link"></i> Matches</a>
            <a class="nav-link" href="/hospital/notifications"><i class="fas fa-bell"></i> Notifications</a>
            <a class="nav-link mt-auto" href="/logout"><i class="fas fa-sign-out-alt"></i> Logout</a>
        </nav>
    </div>

    <div class="main-content">
        <div class="d-flex justify-content-between align-items-center mb-4">
            <div>
                <h2>Welcome, ${user.firstName}!</h2>
                <p class="text-muted">Manage organ transplant matches</p>
            </div>
            <div>
                <span class="badge bg-primary">Hospital</span>
            </div>
        </div>

        <c:choose>
            <c:when test="${hospital == null}">
                <div class="stat-card text-center py-5">
                    <i class="fas fa-hospital-alt fa-4x text-muted mb-4"></i>
                    <h4>Complete Your Hospital Profile</h4>
                    <p class="text-muted">Register your hospital details to manage transplants</p>
                    <a href="/hospital/register" class="btn btn-primary btn-lg mt-3">
                        <i class="fas fa-plus me-2"></i>Register Hospital
                    </a>
                </div>
            </c:when>
            <c:otherwise>
                <div class="row g-4 mb-4">
                    <div class="col-md-4">
                        <div class="stat-card">
                            <h6 class="text-muted">Hospital Name</h6>
                            <h4 class="text-primary">${hospital.hospitalName}</h4>
                        </div>
                    </div>
                    <div class="col-md-4">
                        <div class="stat-card">
                            <h6 class="text-muted">Location</h6>
                            <h5>${hospital.city}, ${hospital.state}</h5>
                        </div>
                    </div>
                    <div class="col-md-4">
                        <div class="stat-card">
                            <h6 class="text-muted">Verification Status</h6>
                            <c:choose>
                                <c:when test="${hospital.isVerified}">
                                    <h4 class="text-success"><i class="fas fa-check-circle"></i> Verified</h4>
                                </c:when>
                                <c:otherwise>
                                    <h4 class="text-warning"><i class="fas fa-clock"></i> Pending</h4>
                                </c:otherwise>
                            </c:choose>
                        </div>
                    </div>
                </div>

                <div class="stat-card">
                    <h5 class="mb-4">Recent Matches</h5>
                    <c:choose>
                        <c:when test="${empty matches}">
                            <p class="text-muted text-center py-4">No matches assigned to your hospital yet.</p>
                        </c:when>
                        <c:otherwise>
                            <table class="table">
                                <thead><tr><th>Donor</th><th>Recipient</th><th>Score</th><th>Status</th><th>Actions</th></tr></thead>
                                <tbody>
                                    <c:forEach items="${matches}" var="match">
                                        <tr>
                                            <td>${match.donor.user.firstName}</td>
                                            <td>${match.recipient.user.firstName}</td>
                                            <td>${match.matchScore}%</td>
                                            <td><span class="badge bg-info">${match.status}</span></td>
                                            <td>
                                                <c:if test="${match.status == 'PENDING'}">
                                                    <form action="/hospital/approve-match/${match.id}" method="post" style="display:inline">
                                                        <button type="submit" class="btn btn-sm btn-success">Approve</button>
                                                    </form>
                                                </c:if>
                                                <c:if test="${match.status == 'APPROVED'}">
                                                    <form action="/hospital/complete-match/${match.id}" method="post" style="display:inline">
                                                        <button type="submit" class="btn btn-sm btn-primary">Complete</button>
                                                    </form>
                                                </c:if>
                                            </td>
                                        </tr>
                                    </c:forEach>
                                </tbody>
                            </table>
                        </c:otherwise>
                    </c:choose>
                </div>
            </c:otherwise>
        </c:choose>
    </div>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
