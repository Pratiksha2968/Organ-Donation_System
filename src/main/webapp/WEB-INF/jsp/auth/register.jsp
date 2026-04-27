<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Register - Organ Donation System</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css" rel="stylesheet">
    <style>
        body {
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            min-height: 100vh;
            padding: 40px 0;
        }
        .register-container {
            background: white;
            border-radius: 20px;
            box-shadow: 0 20px 60px rgba(0,0,0,0.3);
            padding: 40px;
            max-width: 600px;
            margin: 0 auto;
        }
        .btn-primary {
            background: linear-gradient(135deg, #e74c3c 0%, #c0392b 100%);
            border: none;
        }
        .btn-primary:hover {
            background: linear-gradient(135deg, #c0392b 0%, #a93226 100%);
        }
        .form-control, .form-select {
            border-radius: 10px;
            padding: 12px 20px;
        }
        .role-card {
            border: 2px solid #e9ecef;
            border-radius: 15px;
            padding: 20px;
            cursor: pointer;
            transition: all 0.3s;
            text-align: center;
        }
        .role-card:hover, .role-card.active {
            border-color: #e74c3c;
            background: rgba(231, 76, 60, 0.05);
        }
        .role-card i {
            font-size: 2.5rem;
            color: #e74c3c;
            margin-bottom: 10px;
        }
    </style>
</head>
<body>
    <div class="container">
        <div class="register-container">
            <h2 class="text-center mb-4">
                <i class="fas fa-heartbeat text-danger me-2"></i>
                Create Account
            </h2>
            
            <% if(request.getAttribute("error") != null) { %>
                <div class="alert alert-danger"><%= request.getAttribute("error") %></div>
            <% } %>

            <form action="/register" method="post">
                <div class="row">
                    <div class="col-md-6 mb-3">
                        <label class="form-label">First Name</label>
                        <input type="text" class="form-control" name="firstName" required>
                    </div>
                    <div class="col-md-6 mb-3">
                        <label class="form-label">Last Name</label>
                        <input type="text" class="form-control" name="lastName" required>
                    </div>
                </div>
                
                <div class="mb-3">
                    <label class="form-label">Email Address</label>
                    <input type="email" class="form-control" name="email" required>
                </div>
                
                <div class="mb-3">
                    <label class="form-label">Phone Number</label>
                    <input type="tel" class="form-control" name="phone">
                </div>
                
                <div class="mb-3">
                    <label class="form-label">Password</label>
                    <input type="password" class="form-control" name="password" required minlength="6">
                </div>
                
                <div class="mb-4">
                    <label class="form-label">Register As</label>
                    <div class="row g-3">
                        <div class="col-md-4">
                            <div class="role-card" onclick="selectRole('DONOR')">
                                <i class="fas fa-hand-holding-heart"></i>
                                <h5>Donor</h5>
                                <small class="text-muted">Donate organs</small>
                            </div>
                        </div>
                        <div class="col-md-4">
                            <div class="role-card" onclick="selectRole('RECIPIENT')">
                                <i class="fas fa-user-injured"></i>
                                <h5>Recipient</h5>
                                <small class="text-muted">Need an organ</small>
                            </div>
                        </div>
                        <div class="col-md-4">
                            <div class="role-card" onclick="selectRole('HOSPITAL')">
                                <i class="fas fa-hospital"></i>
                                <h5>Hospital</h5>
                                <small class="text-muted">Medical center</small>
                            </div>
                        </div>
                    </div>
                    <input type="hidden" name="role" id="roleInput" value="DONOR">
                </div>
                
                <button type="submit" class="btn btn-primary w-100 py-3">
                    <i class="fas fa-user-plus me-2"></i>Create Account
                </button>
            </form>
            
            <div class="text-center mt-4">
                <p>Already have an account? <a href="/login" style="color: #e74c3c;">Login here</a></p>
            </div>
        </div>
    </div>
    
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
    <script>
        function selectRole(role) {
            document.getElementById('roleInput').value = role;
            document.querySelectorAll('.role-card').forEach(card => card.classList.remove('active'));
            event.currentTarget.classList.add('active');
        }
    </script>
</body>
</html>
