# Simple API Test

Write-Host "Testing API..." -ForegroundColor Green

# Test Login
$body = '{"email":"patient@healthservices.com","password":"patient123"}'

try {
    $response = Invoke-RestMethod -Uri "http://localhost:8080/api/auth/login" -Method Post -ContentType "application/json" -Body $body
    
    Write-Host "SUCCESS! Login works!" -ForegroundColor Green
    Write-Host "User: $($response.data.user.fullName)"
    Write-Host "Role: $($response.data.user.role)"
    Write-Host ""
    Write-Host "Access Token: $($response.data.accessToken.Substring(0,50))..."
    
} catch {
    Write-Host "ERROR: $($_.Exception.Message)" -ForegroundColor Red
    Write-Host "Make sure application is running on http://localhost:8080"
}

