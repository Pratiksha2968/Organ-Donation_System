<!DOCTYPE html>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Admin Dashboard - Organ Donation System</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css" rel="stylesheet">
    <script src="https://cdn.jsdelivr.net/npm/chart.js"></script>
    <style>
        :root {
            --primary-color: #e74c3c;
            --sidebar-width: 260px;
        }
        body {
            background: #f8f9fa;
        }
        .sidebar {
            position: fixed;
            left: 0;
            top: 0;
            width: var(--sidebar-width);
            height: 100vh;
            background: linear-gradient(180deg, #2c3e50 0%, #1a252f 100%);
            padding: 20px;
            color: white;
            z-index: 1000;
        }
        .sidebar .logo {
            text-align: center;
            padding: 20px 0;
            border-bottom: 1px solid rgba(255,255,255,0.1);
            margin-bottom: 20px;
        }
        .sidebar .logo i {
            font-size: 2.5rem;
            color: #e74c3c;
        }
        .sidebar .nav-link {
            color: rgba(255,255,255,0.7);
            padding: 12px 20px;
            border-radius: 10px;
            margin-bottom: 5px;
            transition: all 0.3s;
        }
        .sidebar .nav-link:hover, .sidebar .nav-link.active {
            background: rgba(231, 76, 60, 0.2);
            color: white;
        }
        .sidebar .nav-link i {
            width: 25px;
            margin-right: 10px;
        }
        .main-content {
            margin-left: var(--sidebar-width);
            padding: 30px;
        }
        .stat-card {
            background: white;
            border-radius: 15px;
            padding: 25px;
            box-shadow: 0 5px 20px rgba(0,0,0,0.08);
            transition: transform 0.3s;
        }
        .stat-card:hover {
            transform: translateY(-5px);
        }
        .stat-card .icon {
            width: 60px;
            height: 60px;
            border-radius: 15px;
            display: flex;
            align-items: center;
            justify-content: center;
            font-size: 1.5rem;
        }
        .stat-card h3 {
            font-size: 2rem;
            font-weight: 700;
            margin-bottom: 5px;
        }
        .table-container {
            background: white;
            border-radius: 15px;
            padding: 25px;
            box-shadow: 0 5px 20px rgba(0,0,0,0.08);
        }
        .badge-urgent-critical { background: #dc3545; }
        .badge-urgent-high { background: #fd7e14; }
        .badge-urgent-medium { background: #ffc107; color: #000; }
        .badge-urgent-low { background: #28a745; }
    </style>
</head>
<body>
    <!-- Sidebar -->
    <div class="sidebar">
        <div class="logo">
            <i class="fas fa-heartbeat"></i>
            <h5 class="mt-2">Organ Donation</h5>
            <small>Admin Panel</small>
        </div>
        
        <nav class="nav flex-column">
            <a class="nav-link active" href="/admin/dashboard">
                <i class="fas fa-tachometer-alt"></i> Dashboard
            </a>
            <a class="nav-link" href="/admin/donors">
                <i class="fas fa-hand-holding-heart"></i> Donors
            </a>
            <a class="nav-link" href="/admin/recipients">
                <i class="fas fa-user-injured"></i> Recipients
            </a>
            <a class="nav-link" href="/admin/hospitals">
                <i class="fas fa-hospital"></i> Hospitals
            </a>
            <a class="nav-link" href="/admin/matches">
                <i class="fas fa-link"></i> Matches
            </a>
            <a class="nav-link" href="/admin/analytics">
                <i class="fas fa-chart-bar"></i> Analytics
            </a>
            <a class="nav-link mt-auto" href="/logout">
                <i class="fas fa-sign-out-alt"></i> Logout
            </a>
        </nav>
    </div>

    <!-- Main Content -->
    <div class="main-content">
        <div class="d-flex justify-content-between align-items-center mb-4">
            <div>
                <h2 class="mb-1">Welcome, ${user.firstName}!</h2>
                <p class="text-muted">Here's what's happening with your organ donation system</p>
            </div>
            <div>
                <span class="badge bg-primary">Admin</span>
            </div>
        </div>

        <!-- Stats Cards -->
        <div class="row g-4 mb-4">
            <div class="col-md-3">
                <div class="stat-card">
                    <div class="d-flex justify-content-between">
                        <div>
                            <p class="text-muted mb-1">Total Donors</p>
                            <h3 class="text-primary">${totalDonors}</h3>
                            <small class="text-success"><i class="fas fa-check-circle"></i> ${verifiedDonors} verified</small>
                        </div>
                        <div class="icon bg-primary bg-opacity-10 text-primary">
                            <i class="fas fa-hand-holding-heart"></i>
                        </div>
                    </div>
                </div>
            </div>
            <div class="col-md-3">
                <div class="stat-card">
                    <div class="d-flex justify-content-between">
                        <div>
                            <p class="text-muted mb-1">Recipients</p>
                            <h3 class="text-info">${totalRecipients}</h3>
                            <small class="text-danger"><i class="fas fa-exclamation-circle"></i> ${criticalPatients} critical</small>
                        </div>
                        <div class="icon bg-info bg-opacity-10 text-info">
                            <i class="fas fa-user-injured"></i>
                        </div>
                    </div>
                </div>
            </div>
            <div class="col-md-3">
                <div class="stat-card">
                    <div class="d-flex justify-content-between">
                        <div>
                            <p class="text-muted mb-1">Hospitals</p>
                            <h3 class="text-success">${totalHospitals}</h3>
                            <small class="text-muted">${verifiedHospitals} verified</small>
                        </div>
                        <div class="icon bg-success bg-opacity-10 text-success">
                            <i class="fas fa-hospital"></i>
                        </div>
                    </div>
                </div>
            </div>
            <div class="col-md-3">
                <div class="stat-card">
                    <div class="d-flex justify-content-between">
                        <div>
                            <p class="text-muted mb-1">Transplants</p>
                            <h3 class="text-warning">${completedMatches}</h3>
                            <small class="text-muted">${pendingMatches} pending</small>
                        </div>
                        <div class="icon bg-warning bg-opacity-10 text-warning">
                            <i class="fas fa-heart"></i>
                        </div>
                    </div>
                </div>
            </div>
        </div>

        <!-- Charts Row -->
        <div class="row g-4 mb-4">
            <div class="col-md-8">
                <div class="table-container">
                    <h5 class="mb-4">Organ Demand Overview</h5>
                    <canvas id="organChart" height="100"></canvas>
                </div>
            </div>
            <div class="col-md-4">
                <div class="table-container">
                    <h5 class="mb-4">Urgency Distribution</h5>
                    <canvas id="urgencyChart"></canvas>
                </div>
            </div>
        </div>

        <!-- Recent Activity -->
        <div class="row g-4">
            <div class="col-md-6">
                <div class="table-container">
                    <h5 class="mb-4">Recent Donors</h5>
                    <table class="table table-hover">
                        <thead>
                            <tr>
                                <th>Name</th>
                                <th>Blood Group</th>
                                <th>Status</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach items="${recentDonors}" var="donor">
                                <tr>
                                    <td>${donor.user.firstName} ${donor.user.lastName}</td>
                                    <td><span class="badge bg-danger">${donor.bloodGroup}</span></td>
                                    <td>
                                        <c:choose>
                                            <c:when test="${donor.kycStatus == 'VERIFIED'}">
                                                <span class="badge bg-success">Verified</span>
                                            </c:when>
                                            <c:otherwise>
                                                <span class="badge bg-warning">Pending</span>
                                            </c:otherwise>
                                        </c:choose>
                                    </td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                </div>
            </div>
            <div class="col-md-6">
                <div class="table-container">
                    <h5 class="mb-4">Recent Matches</h5>
                    <table class="table table-hover">
                        <thead>
                            <tr>
                                <th>Match Score</th>
                                <th>Organ</th>
                                <th>Status</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach items="${recentMatches}" var="match">
                                <tr>
                                    <td>
                                        <div class="progress" style="width: 100px; height: 8px;">
                                            <div class="progress-bar bg-success" style="width: ${match.matchScore}%"></div>
                                        </div>
                                        <small>${match.matchScore}%</small>
                                    </td>
                                    <td>${match.recipient.requiredOrgan}</td>
                                    <td><span class="badge bg-info">${match.status}</span></td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                </div>
            </div>
        </div>
    </div>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
    <script>
        // Organ Demand Chart
        new Chart(document.getElementById('organChart'), {
            type: 'bar',
            data: {
                labels: ['Kidney', 'Liver', 'Heart', 'Lung', 'Pancreas'],
                datasets: [{
                    label: 'Recipients Waiting',
                    data: [45, 30, 20, 15, 10],
                    backgroundColor: ['#e74c3c', '#3498db', '#2ecc71', '#f39c12', '#9b59b6']
                }]
            },
            options: {
                responsive: true,
                plugins: { legend: { display: false } }
            }
        });

        // Urgency Chart
        new Chart(document.getElementById('urgencyChart'), {
            type: 'doughnut',
            data: {
                labels: ['Critical', 'High', 'Medium', 'Low'],
                datasets: [{
                    data: [${criticalCount}, ${highCount}, ${mediumCount}, ${lowCount}],
                    backgroundColor: ['#dc3545', '#fd7e14', '#ffc107', '#28a745']
                }]
            }
        });
    </script>
</body>
</html>
