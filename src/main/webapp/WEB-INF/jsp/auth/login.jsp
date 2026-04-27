<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Login - Organ Donation System</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css" rel="stylesheet">
    <style>
        :root {
            --primary-color: #e74c3c;
            --secondary-color: #c0392b;
            --accent-color: #3498db;
        }
        body {
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            min-height: 100vh;
            display: flex;
            align-items: center;
            justify-content: center;
        }
        .login-container {
            background: white;
            border-radius: 20px;
            box-shadow: 0 20px 60px rgba(0,0,0,0.3);
            overflow: hidden;
            max-width: 900px;
            width: 100%;
        }
        .login-left {
            background: linear-gradient(135deg, #e74c3c 0%, #c0392b 100%);
            padding: 60px 40px;
            color: white;
        }
        .login-left h1 {
            font-size: 2.5rem;
            margin-bottom: 20px;
        }
        .login-right {
            padding: 60px 40px;
        }
        .btn-primary {
            background: linear-gradient(135deg, #e74c3c 0%, #c0392b 100%);
            border: none;
            padding: 12px 30px;
            font-weight: 600;
        }
        .btn-primary:hover {
            background: linear-gradient(135deg, #c0392b 0%, #a93226 100%);
        }
        .form-control {
            border-radius: 10px;
            padding: 12px 20px;
        }
        .form-control:focus {
            border-color: #e74c3c;
            box-shadow: 0 0 0 0.2rem rgba(231, 76, 60, 0.25);
        }
        .icon-box {
            width: 80px;
            height: 80px;
            background: rgba(255,255,255,0.2);
            border-radius: 50%;
            display: flex;
            align-items: center;
            justify-content: center;
            margin-bottom: 30px;
        }
        .icon-box i {
            font-size: 2.5rem;
        }
    </style>
</head>
<body>
    <div class="login-container">
        <div class="row g-0">
            <div class="col-md-5 login-left">
                <div class="icon-box">
                    <i class="fas fa-heartbeat"></i>
                </div>
                <h1>Organ Donation</h1>
                <p class="lead">Smart Registry & Matching System</p>
                <p>Join our mission to save lives through organ donation. Our intelligent matching system connects donors with recipients efficiently.</p>
                <ul class="list-unstyled mt-4">
                    <li><i class="fas fa-check-circle me-2"></i> Smart AI-based Matching</li>
                    <li><i class="fas fa-check-circle me-2"></i> Real-time Notifications</li>
                    <li><i class="fas fa-check-circle me-2"></i> Secure & Verified</li>
                </ul>
            </div>
            <div class="col-md-7 login-right">
                <h2 class="mb-4">Welcome Back</h2>
                
                <% if(request.getAttribute("error") != null) { %>
                    <div class="alert alert-danger alert-dismissible fade show" role="alert">
                        <%= request.getAttribute("error") %>
                        <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
                    </div>
                <% } %>
                
                <% if(request.getParameter("registered") != null) { %>
                    <div class="alert alert-success alert-dismissible fade show" role="alert">
                        Registration successful! Please login.
                        <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
                    </div>
                <% } %>
                
                <% if(request.getParameter("logout") != null) { %>
                    <div class="alert alert-info alert-dismissible fade show" role="alert">
                        You have been logged out successfully.
                        <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
                    </div>
                <% } %>

                <form action="/login" method="post">
                    <div class="mb-3">
                        <label class="form-label">Email Address</label>
                        <div class="input-group">
                            <span class="input-group-text"><i class="fas fa-envelope"></i></span>
                            <input type="email" class="form-control" name="username" placeholder="Enter your email" required>
                        </div>
                    </div>
                    <div class="mb-3">
                        <label class="form-label">Password</label>
                        <div class="input-group">
                            <span class="input-group-text"><i class="fas fa-lock"></i></span>
                            <input type="password" class="form-control" name="password" placeholder="Enter your password" required>
                        </div>
                    </div>
                    <div class="mb-3 form-check">
                        <input type="checkbox" class="form-check-input" id="remember">
                        <label class="form-check-label" for="remember">Remember me</label>
                    </div>
                    <button type="submit" class="btn btn-primary w-100 mb-3">
                        <i class="fas fa-sign-in-alt me-2"></i>Login
                    </button>
                </form>
                
                <div class="text-center mt-4">
                    <p>Don't have an account? <a href="/register" class="text-decoration-none fw-bold" style="color: #e74c3c;">Register here</a></p>
                </div>

                <hr class="my-4">
                <p class="text-muted text-center mb-2">Demo Credentials:</p>
                <div class="row text-center small text-muted">
                    <div class="col-6">
                        <strong>Admin:</strong> admin@organdonor.com / admin123
                    </div>
                    <div class="col-6">
                        <strong>Hospital:</strong> hospital@test.com / hospital123
                    </div>
                </div>
            </div>
        </div>
    </div>
    
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
