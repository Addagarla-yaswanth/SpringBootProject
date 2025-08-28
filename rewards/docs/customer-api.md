# Customer API

#1.Create A New Customer.

##Endpoint
POST /customer
## Request Body
json
{
  "custName": "Yaswanth",
  "phoneNo": "9456735890",
  "transaction": [
    { "amount": 120, "date": "2025-08-20" },
    { "amount": 75, "date": "2025-07-22" }
  ]
}

##Successful Response (200 OK)
json
{
  "custId": 2,
  "custName": "Yaswanth",
  "phoneNo": "$2a$10$XhW4nfiPUnL1gVt46QjkW1QpwhxzTXRHhmfdAisPEdQa9vSQFSy", 
  "transaction": [
    { 
"id": 1,
 "date": "2025-08-20",
 "amount": 120,
 "custId": 2 }
  ]
}

#2. Get Total Reward Points (Last 3 Months)
##Endpoint
GET /customer/{custId}/rewards

##Example Request
GET /customer/2/rewards

##Successful Response (200 OK)
Hello Yaswanth, your total reward points for last 3 months are 115.

##Error Response (400 Bad Request)
Customer with ID :1 not found



#3. Get Monthly Reward Points
##Endpoint
GET /customer/{custId}/rewards/{monthOffset}

##Example Request
GET /customer/2/rewards/7

##Successful Response (200 OK)
Hello Yaswanth, your reward points for month offset 7 are 25.

##Error Response (400 Bad Request - Invalid Month Offset)
Month Offset should be between 1 and 12
