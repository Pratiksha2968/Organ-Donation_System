<!DOCTYPE html>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Donor Dashboard - Organ Donation System</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css" rel="stylesheet">
    <style>
        :root { --primary-color: #e74c3c; --sidebar-width: 260px; }
        body { background: #f8f9fa; }
        .sidebar {
            position: fixed; left: 0; top: 0; width: var(--sidebar-width);
            height: 100vh; background: linear-gradient(180deg, #27ae60 0%, #1e8449 100%);
            padding: 20px; color: white; z-index: 1000;
        }
        .sidebar .logo { text-align: center; padding: 20px 0; border-bottom: 1px solid rgba(255,255,255,0.1); margin-bottom: 20px; }
        .sidebar .logo i { font-size: 2.5rem; }
        .sidebar .nav-link { color: rgba(255,255,255,0.8); padding: 12px 20px; border-radius: 10px; margin-bottom: 5px; }
        .sidebar .nav-link:hover, .sidebar .nav-link.active { background: rgba(255,255,255,0.2); color: white; }
        .main-content { margin-left: var(--sidebar-width); padding: 30px; }
        .stat-card { background: white; border-radius: 15px; padding: 25px; box-shadow: 0 5px 20px rgba(0,0,0,0.08); }
        .emergency-toggle { width: 60px; height: 30px; }
        .emergency-toggle:checked { background-color: #27ae60; }
    </style>
</head>
<body>
    <div class="sidebar">
        <div class="logo">
            <i class="fas fa-hand-holding-heart"></i>
            <h5 class="mt-2">Donor Portal</h5>
        </div>
        <nav class="nav flex-column">
            <a class="nav-link active" href="/donor/dashboard"><i class="fas fa-tachometer-alt"></i> Dashboard</a>
            <a class="nav-link" href="/donor/profile"><i class="fas fa-user"></i> My Profile</a>
            <a class="nav-link" href="/donor/notifications"><i class="fas fa-bell"></i> Notifications</a>
            <a class="nav-link mt-auto" href="/logout"><i class="fas fa-sign-out-alt"></i> Logout</a>
        </nav>
    </div>

    <div class="main-content">
        <div class="d-flex justify-content-between align-items-center mb-4">
            <div>
                <h2>Welcome, ${user.firstName}!</h2>
                <p class="text-muted">Thank you for being a donor</p>
            </div>
            <div class="d-flex align-items-center gap-3">
                <span class="badge bg-success">Donor</span>
                <a href="/donor/notifications" class="btn btn-outline-primary position-relative">
                    <i class="fas fa-bell"></i>
                    <c:if test="${unreadNotifications > 0}">
                        <span class="position-absolute top-0 start-100 translate-middle badge rounded-pill bg-danger">${unreadNotifications}</span>
                    </c:if>
                </a>
            </div>
        </div>

        <c:choose>
            <c:when test="${donor == null}">
                <div class="stat-card text-center py-5">
                    <i class="fas fa-file-medical fa-4x text-muted mb-4"></i>
                    <h4>Complete Your Donor Profile</h4>
                    <p class="text-muted">Register your donation preferences to help save lives</p>
                    <a href="/donor/register" class="btn btn-success btn-lg mt-3">
                        <i class="fas fa-plus me-2"></i>Register as Donor
                    </a>
                </div>
            </c:when>
            <c:otherwise>
                <div class="row g-4 mb-4">
                    <div class="col-md-4">
                        <div class="stat-card">
                            <h6 class="text-muted">Blood Group</h6>
                            <h2 class="text-danger">${donor.bloodGroup}</h2>
                        </div>
                    </div>
                    <div class="col-md-4">
                        <div class="stat-card">
                            <h6 class="text-muted">Organs to Donate</h6>
                            <div class="mt-2">
                                <c:forEach items="${donor.organsToDonate}" var="organ">
                                    <span class="badge bg-primary me-1">${organ}</span>
                                </c:forEach>
                            </div>
                        </div>
                    </div>
                    <div class="col-md-4">
                        <div class="stat-card">
                            <h6 class="text-muted">Verification Status</h6>
                            <c:choose>
                                <c:when test="${donor.kycStatus == 'VERIFIED'}">
                                    <h4 class="text-success"><i class="fas fa-check-circle"></i> Verified</h4>
                                </c:when>
                                <c:otherwise>
                                    <h4 class="text-warning"><i class="fas fa-clock"></i> Pending</h4>
                                </c:otherwise>
                            </c:choose>
                        </div>
                    </div>
                </div>

                <div class="stat-card mb-4">
                    <div class="d-flex justify-content-between align-items-center">
                        <div>
                            <h5><i class="fas fa-ambulance text-danger me-2"></i>Emergency Availability</h5>
                            <p class="text-muted mb-0">Toggle your availability for urgent donations</p>
                        </div>
                        <form action="/donor/toggle-emergency" method="post">
                            <div class="form-check form-switch">
                                <input class="form-check-input emergency-toggle" type="checkbox" 
                                       ${donor.isEmergencyAvailable ? 'checked' : ''} onchange="this.form.submit()">
                            </div>
                        </form>
                    </div>
                </div>

                <div class="stat-card">
                    <h5 class="mb-4">Your Matches</h5>
                    <c:choose>
                        <c:when test="${empty matches}">
                            <p class="text-muted text-center py-4">No matches found yet. We'll notify you when a match is found!</p>
                        </c:when>
                        <c:otherwise>
                            <table class="table">
                                <thead><tr><th>Recipient</th><th>Organ</th><th>Score</th><th>Status</th></tr></thead>
                                <tbody>
                                    <c:forEach items="${matches}" var="match">
                                        <tr>
                                            <td>${match.recipient.user.firstName}</td>
                                            <td>${match.recipient.requiredOrgan}</td>
                                            <td>${match.matchScore}%</td>
                                            <td><span class="badge bg-info">${match.status}</span></td>
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
