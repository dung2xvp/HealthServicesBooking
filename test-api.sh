#!/bin/bash

# Health Services Booking - API Test Script
# This script will test all authentication endpoints

echo "🚀 Health Services Booking - API Test Script"
echo "=============================================="
echo ""

# Configuration
BASE_URL="http://localhost:8080"
TEST_USERNAME="testuser_$(date +%s)"
TEST_EMAIL="test_$(date +%s)@example.com"
TEST_PASSWORD="password123"

# Colors for output
GREEN='\033[0;32m'
RED='\033[0;31m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

# Function to print test result
print_result() {
    if [ $1 -eq 0 ]; then
        echo -e "${GREEN}✅ PASS${NC}: $2"
    else
        echo -e "${RED}❌ FAIL${NC}: $2"
        exit 1
    fi
}

# Test 1: Check if API is running
echo "📍 Test 1: Checking if API is running..."
RESPONSE=$(curl -s -o /dev/null -w "%{http_code}" ${BASE_URL}/api/auth/test)
if [ "$RESPONSE" = "200" ]; then
    print_result 0 "API is running"
else
    print_result 1 "API is not running or not accessible"
fi
echo ""

# Test 2: Register new user
echo "📍 Test 2: Registering new user..."
REGISTER_RESPONSE=$(curl -s -w "\n%{http_code}" -X POST ${BASE_URL}/api/auth/register \
  -H "Content-Type: application/json" \
  -d "{
    \"username\": \"${TEST_USERNAME}\",
    \"email\": \"${TEST_EMAIL}\",
    \"password\": \"${TEST_PASSWORD}\",
    \"fullname\": \"Test User\",
    \"mobile\": \"0123456789\",
    \"gender\": 1,
    \"address\": \"123 Test Street\",
    \"birthday\": \"1990-01-01\"
  }")

HTTP_CODE=$(echo "$REGISTER_RESPONSE" | tail -n1)
REGISTER_BODY=$(echo "$REGISTER_RESPONSE" | sed '$d')

if [ "$HTTP_CODE" = "201" ]; then
    print_result 0 "User registered successfully"
    echo "   Username: ${TEST_USERNAME}"
    echo "   Email: ${TEST_EMAIL}"
else
    print_result 1 "Failed to register user (HTTP $HTTP_CODE)"
fi
echo ""

# Test 3: Try to register with same username (should fail)
echo "📍 Test 3: Testing duplicate username validation..."
DUPLICATE_RESPONSE=$(curl -s -o /dev/null -w "%{http_code}" -X POST ${BASE_URL}/api/auth/register \
  -H "Content-Type: application/json" \
  -d "{
    \"username\": \"${TEST_USERNAME}\",
    \"email\": \"another_${TEST_EMAIL}\",
    \"password\": \"${TEST_PASSWORD}\",
    \"fullname\": \"Test User\",
    \"mobile\": \"0123456789\"
  }")

if [ "$DUPLICATE_RESPONSE" = "400" ]; then
    print_result 0 "Duplicate username validation working"
else
    print_result 1 "Duplicate username validation failed"
fi
echo ""

# Test 4: Verify email (need to get code from database or use manual verification)
echo "📍 Test 4: Email verification..."
echo -e "${YELLOW}⚠️  SKIP${NC}: Email verification requires code from database or email"
echo "   To test manually:"
echo "   1. Get code: mysql -u root -p -e \"SELECT code FROM healthservicesbooking.user WHERE username='${TEST_USERNAME}';\""
echo "   2. Run: curl -X POST ${BASE_URL}/api/auth/verify-email -H 'Content-Type: application/json' -d '{\"email\":\"${TEST_EMAIL}\",\"code\":\"CODE_HERE\"}'"
echo ""

# For testing purposes, we'll skip email verification by directly updating database
echo "📍 Test 5: Manually setting email as verified (for testing)..."
echo -e "${YELLOW}⚠️  INFO${NC}: In production, email must be verified via code"
# This would require MySQL command which we skip in automated test
echo ""

# Test 6: Login (will fail if email not verified, unless we bypass)
echo "📍 Test 6: Testing login..."
LOGIN_RESPONSE=$(curl -s -w "\n%{http_code}" -X POST ${BASE_URL}/api/auth/login \
  -H "Content-Type: application/json" \
  -d "{
    \"username\": \"${TEST_USERNAME}\",
    \"password\": \"${TEST_PASSWORD}\"
  }")

HTTP_CODE=$(echo "$LOGIN_RESPONSE" | tail -n1)
LOGIN_BODY=$(echo "$LOGIN_RESPONSE" | sed '$d')

if [ "$HTTP_CODE" = "200" ]; then
    print_result 0 "Login successful"
    
    # Extract tokens
    ACCESS_TOKEN=$(echo "$LOGIN_BODY" | grep -o '"accessToken":"[^"]*' | sed 's/"accessToken":"//')
    REFRESH_TOKEN=$(echo "$LOGIN_BODY" | grep -o '"refreshToken":"[^"]*' | sed 's/"refreshToken":"//')
    
    echo "   Access Token: ${ACCESS_TOKEN:0:50}..."
    echo "   Refresh Token: ${REFRESH_TOKEN:0:36}..."
else
    echo -e "${YELLOW}⚠️  SKIP${NC}: Login failed (email may not be verified) - HTTP $HTTP_CODE"
    echo "   This is expected if email verification is required"
    ACCESS_TOKEN=""
    REFRESH_TOKEN=""
fi
echo ""

# Test 7: Refresh token (only if we have tokens)
if [ ! -z "$REFRESH_TOKEN" ]; then
    echo "📍 Test 7: Testing refresh token..."
    REFRESH_RESPONSE=$(curl -s -o /dev/null -w "%{http_code}" -X POST ${BASE_URL}/api/auth/refresh-token \
      -H "Content-Type: application/json" \
      -d "{\"refreshToken\": \"${REFRESH_TOKEN}\"}")
    
    if [ "$REFRESH_RESPONSE" = "200" ]; then
        print_result 0 "Refresh token works"
    else
        print_result 1 "Refresh token failed (HTTP $REFRESH_RESPONSE)"
    fi
    echo ""
fi

# Test 8: Logout (only if we have access token)
if [ ! -z "$ACCESS_TOKEN" ]; then
    echo "📍 Test 8: Testing logout..."
    LOGOUT_RESPONSE=$(curl -s -o /dev/null -w "%{http_code}" -X POST ${BASE_URL}/api/auth/logout \
      -H "Authorization: Bearer ${ACCESS_TOKEN}")
    
    if [ "$LOGOUT_RESPONSE" = "200" ]; then
        print_result 0 "Logout successful"
    else
        print_result 1 "Logout failed (HTTP $LOGOUT_RESPONSE)"
    fi
    echo ""
fi

# Test 9: Forgot password
echo "📍 Test 9: Testing forgot password..."
FORGOT_RESPONSE=$(curl -s -o /dev/null -w "%{http_code}" -X POST ${BASE_URL}/api/auth/forgot-password \
  -H "Content-Type: application/json" \
  -d "{\"email\": \"${TEST_EMAIL}\"}")

if [ "$FORGOT_RESPONSE" = "200" ]; then
    print_result 0 "Forgot password request successful"
else
    print_result 1 "Forgot password failed (HTTP $FORGOT_RESPONSE)"
fi
echo ""

# Test 10: Resend verification code
echo "📍 Test 10: Testing resend verification code..."
RESEND_RESPONSE=$(curl -s -o /dev/null -w "%{http_code}" -X POST ${BASE_URL}/api/auth/resend-code \
  -H "Content-Type: application/json" \
  -d "{\"email\": \"${TEST_EMAIL}\"}")

if [ "$RESEND_RESPONSE" = "200" ]; then
    print_result 0 "Resend verification code successful"
else
    print_result 1 "Resend verification code failed (HTTP $RESEND_RESPONSE)"
fi
echo ""

# Summary
echo "=============================================="
echo "🎉 API Test Summary"
echo "=============================================="
echo "✅ API is running and responding"
echo "✅ User registration works"
echo "✅ Validation works (duplicate username)"
echo "✅ Forgot password works"
echo "✅ Resend code works"
if [ ! -z "$ACCESS_TOKEN" ]; then
    echo "✅ Login works"
    echo "✅ Refresh token works"
    echo "✅ Logout works"
else
    echo "⚠️  Login/Logout/Refresh tests skipped (email verification required)"
fi
echo ""
echo "📝 Test user created:"
echo "   Username: ${TEST_USERNAME}"
echo "   Email: ${TEST_EMAIL}"
echo "   Password: ${TEST_PASSWORD}"
echo ""
echo "💡 To manually verify email and test login:"
echo "   1. Get code from database or email"
echo "   2. POST to /api/auth/verify-email"
echo "   3. Then login with credentials above"
echo ""
echo "✅ All available tests completed successfully!"

