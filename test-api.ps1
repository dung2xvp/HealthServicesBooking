# Test API Script for Health Services Booking

Write-Host "🚀 Testing Health Services Booking API..." -ForegroundColor Green
Write-Host ""

# Test 1: Login with Patient account
Write-Host "📝 Test 1: Login with Patient account" -ForegroundColor Yellow
$loginBody = @{
    email = "patient@healthservices.com"
    password = "patient123"
} | ConvertTo-Json

try {
    $response = Invoke-RestMethod -Uri "http://localhost:8080/api/auth/login" `
        -Method Post `
        -ContentType "application/json" `
        -Body $loginBody
    
    Write-Host "✅ Login successful!" -ForegroundColor Green
    Write-Host "User: $($response.data.user.fullName)" -ForegroundColor Cyan
    Write-Host "Role: $($response.data.user.role)" -ForegroundColor Cyan
    
    $token = $response.data.accessToken
    Write-Host ""
    Write-Host "🔑 Access Token:" -ForegroundColor Yellow
    Write-Host $token.Substring(0, 50)... -ForegroundColor Gray
    Write-Host ""
    
    # Test 2: Get Appointments
    Write-Host "📝 Test 2: Get Patient Appointments" -ForegroundColor Yellow
    $headers = @{
        Authorization = "Bearer $token"
    }
    
    $appointments = Invoke-RestMethod -Uri "http://localhost:8080/api/patient/appointments" `
        -Method Get `
        -Headers $headers
    
    Write-Host "✅ Retrieved appointments: $($appointments.data.Count)" -ForegroundColor Green
    Write-Host ""
    
    # Test 3: Login with Doctor
    Write-Host "📝 Test 3: Login with Doctor account" -ForegroundColor Yellow
    $doctorLoginBody = @{
        email = "doctor@healthservices.com"
        password = "doctor123"
    } | ConvertTo-Json
    
    $doctorResponse = Invoke-RestMethod -Uri "http://localhost:8080/api/auth/login" `
        -Method Post `
        -ContentType "application/json" `
        -Body $doctorLoginBody
    
    Write-Host "✅ Doctor login successful!" -ForegroundColor Green
    Write-Host "Doctor: $($doctorResponse.data.user.fullName)" -ForegroundColor Cyan
    Write-Host ""
    
    # Test 4: Login with Admin
    Write-Host "📝 Test 4: Login with Admin account" -ForegroundColor Yellow
    $adminLoginBody = @{
        email = "admin@healthservices.com"
        password = "admin123"
    } | ConvertTo-Json
    
    $adminResponse = Invoke-RestMethod -Uri "http://localhost:8080/api/auth/login" `
        -Method Post `
        -ContentType "application/json" `
        -Body $adminLoginBody
    
    Write-Host "✅ Admin login successful!" -ForegroundColor Green
    Write-Host "Admin: $($adminResponse.data.user.fullName)" -ForegroundColor Cyan
    Write-Host ""
    
    # Test 5: Get all facilities (Admin)
    Write-Host "📝 Test 5: Get all health facilities" -ForegroundColor Yellow
    $adminHeaders = @{
        Authorization = "Bearer $($adminResponse.data.accessToken)"
    }
    
    $facilities = Invoke-RestMethod -Uri "http://localhost:8080/api/admin/facilities" `
        -Method Get `
        -Headers $adminHeaders
    
    Write-Host "✅ Retrieved facilities: $($facilities.data.Count)" -ForegroundColor Green
    foreach ($facility in $facilities.data) {
        Write-Host "  - $($facility.name) ($($facility.type))" -ForegroundColor Cyan
    }
    Write-Host ""
    
    # Test 6: Get all services
    Write-Host "📝 Test 6: Get all medical services" -ForegroundColor Yellow
    $services = Invoke-RestMethod -Uri "http://localhost:8080/api/admin/services" `
        -Method Get `
        -Headers $adminHeaders
    
    Write-Host "✅ Retrieved services: $($services.data.Count)" -ForegroundColor Green
    foreach ($service in $services.data) {
        Write-Host "  - $($service.name): $($service.price) VNĐ" -ForegroundColor Cyan
    }
    Write-Host ""
    
    # Test 7: Get all doctors
    Write-Host "📝 Test 7: Get all doctors" -ForegroundColor Yellow
    $doctors = Invoke-RestMethod -Uri "http://localhost:8080/api/admin/doctors" `
        -Method Get `
        -Headers $adminHeaders
    
    Write-Host "✅ Retrieved doctors: $($doctors.data.Count)" -ForegroundColor Green
    foreach ($doctor in $doctors.data) {
        Write-Host "  - Dr. $($doctor.user.fullName) - $($doctor.specialization)" -ForegroundColor Cyan
    }
    Write-Host ""
    
    Write-Host "🎉 All tests passed successfully!" -ForegroundColor Green
    Write-Host ""
    Write-Host "💡 Tip: Use the access tokens above to test other APIs" -ForegroundColor Yellow
    Write-Host "   Patient Token: $($token.Substring(0, 30))..." -ForegroundColor Gray
    Write-Host "   Doctor Token: $($doctorResponse.data.accessToken.Substring(0, 30))..." -ForegroundColor Gray
    Write-Host "   Admin Token: $($adminResponse.data.accessToken.Substring(0, 30))..." -ForegroundColor Gray
    
} catch {
    Write-Host "❌ Error: $($_.Exception.Message)" -ForegroundColor Red
    Write-Host ""
    Write-Host "💡 Make sure the application is running on http://localhost:8080" -ForegroundColor Yellow
    Write-Host "   Run: .\mvnw.cmd spring-boot:run" -ForegroundColor Gray
}

Write-Host ""
Write-Host "For more tests, see TEST_API.md" -ForegroundColor Cyan

