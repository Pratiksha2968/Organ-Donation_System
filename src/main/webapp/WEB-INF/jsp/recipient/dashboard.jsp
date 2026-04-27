<!DOCTYPE html>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Recipient Dashboard - Organ Donation System</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css" rel="stylesheet">
    <style>
        :root { --primary-color: #3498db; --sidebar-width: 260px; }
        body { background: #f8f9fa; }
        .sidebar {
            position: fixed; left: 0; top: 0; width: var(--sidebar-width);
            height: 100vh; background: linear-gradient(180deg, #3498db 0%, #2980b9 100%);
            padding: 20px; color: white; z-index: 1000;
        }
        .sidebar .logo { text-align: center; padding: 20px 0; border-bottom: 1px solid rgba(255,255,255,0.1); margin-bottom: 20px; }
        .sidebar .nav-link { color: rgba(255,255,255,0.8); padding: 12px 20px; border-radius: 10px; margin-bottom: 5px; }
        .sidebar .nav-link:hover, .sidebar .nav-link.active { background: rgba(255,255,255,0.2); color: white; }
        .main-content { margin-left: var(--sidebar-width); padding: 30px; }
        .stat-card { background: white; border-radius: 15px; padding: 25px; box-shadow: 0 5px 20px rgba(0,0,0,0.08); }
        .match-card { border: 1px solid #e9ecef; border-radius: 15px; padding: 20px; margin-bottom: 15px; transition: all 0.3s; }
        .match-card:hover { border-color: #3498db; box-shadow: 0 5px 20px rgba(52, 152, 219, 0.2); }
        .score-circle { width: 60px; height: 60px; border-radius: 50%; display: flex; align-items: center; justify-content: center; font-weight: bold; }
    </style>
</head>
<body>
    <div class="sidebar">
        <div class="logo">
            <i class="fas fa-user-injured"></i>
            <h5 class="mt-2">Recipient Portal</h5>
        </div>
        <nav class="nav flex-column">
            <a class="nav-link active" href="/recipient/dashboard"><i class="fas fa-tachometer-alt"></i> Dashboard</a>
            <a class="nav-link" href="/recipient/find-match"><i class="fas fa-search"></i> Find Match</a>
            <a class="nav-link" href="/recipient/profile"><i class="fas fa-user"></i> My Profile</a>
            <a class="nav-link" href="/recipient/notifications"><i class="fas fa-bell"></i> Notifications</a>
            <a class="nav-link mt-auto" href="/logout"><i class="fas fa-sign-out-alt"></i> Logout</a>
        </nav>
    </div>

    <div class="main-content">
        <div class="d-flex justify-content-between align-items-center mb-4">
            <div>
                <h2>Welcome, ${user.firstName}!</h2>
                <p class="text-muted">We're here to help you find a match</p>
            </div>
            <div>
                <span class="badge bg-primary">Recipient</span>
            </div>
        </div>

        <c:choose>
            <c:when test="${recipient == null}">
                <div class="stat-card text-center py-5">
                    <i class="fas fa-heart fa-4x text-muted mb-4"></i>
                    <h4>Complete Your Recipient Profile</h4>
                    <p class="text-muted">Register your organ requirement to find matching donors</p>
                    <a href="/recipient/register" class="btn btn-primary btn-lg mt-3">
                        <i class="fas fa-plus me-2"></i>Register as Recipient
                    </a>
                </div>
            </c:when>
            <c:otherwise>
                <div class="row g-4 mb-4">
                    <div class="col-md-3">
                        <div class="stat-card">
                            <h6 class="text-muted">Required Organ</h6>
                            <h3 class="text-primary">${recipient.requiredOrgan}</h3>
                        </div>
                    </div>
                    <div class="col-md-3">
                        <div class="stat-card">
                            <h6 class="text-muted">Blood Group</h6>
                            <h3 class="text-danger">${recipient.bloodGroup}</h3>
                        </div>
                    </div>
                    <div class="col-md-3">
                        <div class="stat-card">
                            <h6 class="text-muted">Urgency Level</h6>
                            <h3>
                                <span class="badge bg-${recipient.urgencyLevel == 'CRITICAL' ? 'danger' : recipient.urgencyLevel == 'HIGH' ? 'warning' : 'success'}">
                                    ${recipient.urgencyLevel}
                                </span>
                            </h3>
                        </div>
                    </div>
                    <div class="col-md-3">
                        <div class="stat-card">
                            <h6 class="text-muted">Priority Score</h6>
                            <h3 class="text-info">${recipient.priorityScore}</h3>
                        </div>
                    </div>
                </div>

                <div class="stat-card mb-4">
                    <div class="d-flex justify-content-between align-items-center mb-3">
                        <h5><i class="fas fa-bolt text-warning me-2"></i>Quick Actions</h5>
                    </div>
                    <div class="row g-3">
                        <div class="col-md-6">
                            <a href="/recipient/find-match" class="btn btn-outline-primary w-100 py-3">
                                <i class="fas fa-search me-2"></i>Find Matching Donors
                            </a>
                        </div>
                        <div class="col-md-6">
                            <form action="/recipient/quick-match" method="post">
                                <button type="submit" class="btn btn-danger w-100 py-3">
                                    <i class="fas fa-bolt me-2"></i>Emergency Quick Match
                                </button>
                            </form>
                        </div>
                    </div>
                </div>

                <div class="stat-card">
                    <h5 class="mb-4">Your Matches</h5>
                    <c:choose>
                        <c:when test="${empty matches}">
                            <p class="text-muted text-center py-4">No matches found yet. Use "Find Match" to search for donors!</p>
                        </c:when>
                        <c:otherwise>
                            <c:forEach items="${matches}" var="match">
                                <div class="match-card">
                                    <div class="row align-items-center">
                                        <div class="col-md-2">
                                            <div class="score-circle bg-success text-white">
                                                ${match.matchScore}%
                                            </div>
                                        </div>
                                        <div class="col-md-6">
                                            <h6 class="mb-1">Donor: ${match.donor.user.firstName}</h6>
                                            <p class="mb-0 text-muted">
                                                <i class="fas fa-tint me-1"></i>${match.donor.bloodGroup} | 
                                                <i class="fas fa-map-marker-alt me-1"></i>${match.donor.city}
                                            </p>
                                        </div>
                                        <div class="col-md-2">
                                            <span class="badge bg-info">${match.status}</span>
                                        </div>
                                        <div class="col-md-2 text-end">
                                            <small class="text-muted">${match.distanceKm != null ? match.distanceKm + ' km' : 'N/A'}</small>
                                        </div>
                                    </div>
                                </div>
                            </c:forEach>
                        </c:otherwise>
                    </c:choose>
                </div>
            </c:otherwise>
        </c:choose>
    </div>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
