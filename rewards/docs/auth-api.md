# Authentication API

# 1. Generate JWT Token

## Endpoint
POST /customer/authenticate

## Request Body
json
{
"custName": "Yaswanth",
"phoneNo": "9456735890"
}

##Successful Response (200 OK)
eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJZYXdhbnRoIiwiaWF0IjoxNjMzgzNiwiZXhwIjoxNzU0NjYyMzYwfQ.nNhEdmVyEdAMZk6S-TM9TtxlPp0ubvWEd2zYK6cjv8

This is the JWT token string. Use it in the Authorization header for subsequent requests.

Authorization: Bearer <token>
##Error Response (403 Forbidden)
## Request Body
json
{
"error": "Invalid username or password"
}

