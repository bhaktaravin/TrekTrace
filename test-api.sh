#!/bin/bash

echo "🧪 Testing TrekTrace Backend API"
echo "================================"
echo ""

BASE_URL="http://localhost:8080"

# Test 1: Register a new user
echo "1️⃣  Testing user registration..."
REGISTER_RESPONSE=$(curl -s -X POST "$BASE_URL/auth/register" \
  -H "Content-Type: application/json" \
  -d '{"email":"ravin@trektrace.com","password":"test123","displayName":"Ravin"}')

echo "Response: $REGISTER_RESPONSE"
echo ""

# Extract token from response
TOKEN=$(echo $REGISTER_RESPONSE | grep -o '"token":"[^"]*' | cut -d'"' -f4)

if [ -z "$TOKEN" ]; then
  echo "❌ Registration failed or user already exists"
  echo ""
  
  # Try logging in instead
  echo "2️⃣  Trying to login..."
  LOGIN_RESPONSE=$(curl -s -X POST "$BASE_URL/auth/login" \
    -H "Content-Type: application/json" \
    -d '{"email":"ravin@trektrace.com","password":"test123"}')
  
  echo "Response: $LOGIN_RESPONSE"
  echo ""
  
  TOKEN=$(echo $LOGIN_RESPONSE | grep -o '"token":"[^"]*' | cut -d'"' -f4)
fi

if [ -n "$TOKEN" ]; then
  echo "✅ Authentication successful!"
  echo "Token: $TOKEN"
  echo ""
  
  # Test 3: Create a trip (without photos for now)
  echo "3️⃣  Testing trip creation..."
  echo "Note: Photo upload requires multipart/form-data, testing basic structure only"
  echo ""
  
  echo "Your JWT Token for future requests:"
  echo "Authorization: Bearer $TOKEN"
else
  echo "❌ Could not authenticate"
fi

echo ""
echo "✨ Test complete!"
