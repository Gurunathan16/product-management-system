# 🛒 Product Management System

A Spring Boot-based backend service that provides secure user authentication and full-featured product management with warranty tracking and filtering. It uses **JWT for stateless authentication, Redis for session management, and MySQL** for data persistence.

## 🔧 Tech Stack

- **Language**: Java 23
- **Framework**: Spring Boot
- **Security**: Spring Security, JWT (Access + Refresh Tokens)
- **Database**: MySQL
- **ORM**: Spring Data JPA, Hibernate
- **Caching**: Redis (for token/session management)
- **Mailing**: Java Mail Sender
- **Validation**: Hibernate Validator (JSR-380)
- **Mapper**: MapStruct
- **Build Tool**: Gradle

## 🚀 Features

### 🔐 Authentication & Authorization
- ✅ JWT-based stateless authentication (access & refresh tokens)
- ✅ Secure session control using Redis for refresh token storage
- ✅ Password encryption with BCrypt
- ✅ Tokens sent via secure HTTP-only cookies
- ✅ Custom `@MinAge` annotation for signup validation
- ✅ Global exception handling for JWT and auth-related issues
- ✅ Account & credential validity check using `lastLoginDate`

### 👤 User Management
- ✅ User registration, login, and refresh token support
- ✅ Edit user profile (non-sensitive fields only)
- ✅ Change password securely
- ✅ Delete user account
- ✅ No use of `PathVariable` for sensitive operations

### 📦 Product Management
- ✅ Full CRUD support for products
- ✅ Prevents duplicate products per user
- ✅ Paginated product listing
- ✅ Structured DTOs and validations with MapStruct & Hibernate Validator

### 📊 Product Filters & Views
- ✅ View products by:
  - Company
  - Category
  - Usage Location
- ✅ Dropdown-style exploration for grouped products
- ✅ Tag-based filtered product views

### 🔔 Warranty Reminder System
- ✅ Monthly scheduled emails for upcoming warranty expirations
- ✅ Automatically detects products whose warranty expires within the month
- ✅ Users can toggle reminders ON/OFF

### 📬 Communication
- ✅ Email reminders sent using Spring's JavaMailSender

### 🧹 Code Quality & Maintainability
- ✅ DTO mapping via MapStruct
- ✅ Clean and structured `ResponseEntity` responses
- ✅ Strong validation on all user and product inputs
- ✅ Global exception handling and clear layer separation

## 🔗 [Postman Collection](https://www.postman.com/gurunat16/workspace/rentease-backend/collection/42371256-b8da68da-7908-4db2-8ade-9ce61fee4b9c?action=share&creator=42371256)

# 📃 API DOCUMENTATION
<details>
<summary><strong> 🔐 Authentication Endpoints </strong></summary>
   
### 1. **LOGIN** 

**POST** `/auth/login` 

Authenticate a user - returns access and refresh tokens as cookies.
Include the following properties as *body*:

- `username` - String - Required  
- `password` - String - Required  

#### Request Body
```json
{
    "username": "john_doe123",
    "password": "Abc@1234"
}
```
#### Response(200 OK)
*Tokens generated and returned as cookies.*
 - `accessToken` - Short lived token
 - `refreshToken` - Long lived token
```json
{
    "status": "Success",
    "message": "Tokens generated",
    "payload": {
        "sub": <Token subject>,
        "iat": <Issued at time>,
        "exp": <Expiry time>
    }
}
```

**Tokens at Cookies after login**
![Tokens at Cookies](./screenshots/Tokens%20at%20Cookies%20after%20login.png)

#### Response(401 UNAUTHORIZED)
*Authentication failed due to invalid username or password.*
```json
{
    "status": "Unauthorized",
    "message": "Bad Credentials. Authentication failed.",
    "Validation Error": "Invalid username or password"
}
```

### 2. **REGISTER** 

**POST**	`/auth/signup`

Register a new user. 
Include the following properties as *body*:

- `firstName` - String - Required  
- `lastName` - String - Optional
- `gender` - String - Required  
- `dateOfBirth` - LocalDate - Required  
- `username` - String - Required  
- `password` - String - Required  
- `confirmPassword` - String - Required  
- `mailId` - String - Required  
- `phoneNumber` - String - Required  

#### Request Body JSON
```json
{
    "firstName": "John",
    "lastName": "Doe",
    "dateOfBirth": "1995-08-15",
    "gender": "Male",
    "username": "john_doe123",
    "password": "Abc@1234",
    "confirmPassword": "Abc@1234",
    "mailId": "john.doe@example.com",
    "phoneNumber": "+911234567890"
}
```

#### Response(200 OK)
*User registration completed successfully.*
```json
{
    "status": "Created",
    "message": "User registered successfully.",
    "Details": "John"
}
```

#### Response(409 CONFLICT)
*Occurs when the Username or Email ID already exists in the system.*
```json
{
    "status": "Conflict",
    "message": "Username already in use.",
    "Recovery": "Retry with different username."
}

{
    "status": "Conflict",
    "message": "Email ID already in use.",
    "Recovery": "Try login with existing account."
}
```

#### Response(400 BAD REQUEST)
*Registartion failed due to password - confirm password mismatch.*
```json
{
    "status": "Bad Request",
    "message": "Password - Confirm Password Mismatch",
    "Recovery": "Password and Confirm Password should be same."
}
```

### 3. **UPDATE** 

**POST**	`/auth/update`

Update user profile details. Authentication required.
Include the following properties as *body*:

- `firstName` - String - Required  
- `lastName` - String - Optional  
- `gender` - String - Required  
- `dateOfBirth` - LocalDate - Required  

#### Request Body JSON
```json
{
    "firstName": "Johnny",
    "lastName": "Doe",
    "gender": "Male",
    "dateOfBirth": "1995-09-15"
}
```

#### Response(200 OK)
*User details updated successfully.*
```json
{
    "status": "OK",
    "message": "User Profile updated successfully",
    "Details": "Johnny"
}
```

### 4. **CHANGE PASSWORD** 

**POST**	`/auth/changePassword`

Change user password. Authentication required.
Include the following properties as *body*:

- `oldPassword` - String - Required  
- `password` - String - Required  
- `confirmPassword` - String - Required  

#### Request Body JSON
```json
{
    "oldPassword": "Abc@1234",
    "password": "New@Pass2",
    "confirmPassword": "New@Pass2"
}

```
#### Response(200 OK)
*User password updated successfully.*
```json
{
    "status": "OK",
    "message": "Change Password request processed successfully.",
    "Details": "Johnny"
}
```

#### Response(403 FORBIDDEN)
*The provided old password is incorrect.*
```json
{
    "status": "Forbidden",
    "message": "Old Password - Incorrect",
    "Recovery": "Enter correct Old Password"
}
```

#### Response(400 BAD REQUEST)
*Updation failed due to password - confirm password mismatch.*
```json
{
    "status": "Bad Request",
    "message": "Password - Confirm Password Mismatch",
    "Recovery": "Password and Confirm Password should be same."
}
```

### 5. **PROFILE** 

**GET**	`/auth/profile`

Returns the logged-in user's profile. Authentication required. 

#### Response(200 OK)
*User profile retrieved successfully.*
```json
{
    "status": "OK",
    "message": "User profile fetch successful.",
    "Details": {
      "firstName": "Johnny",
      "lastName": "Doe",
      "username": "john_doe123",
      "dateOfBirth": "1995-09-15",
      "gender": "Male",
      "mailId": "john.doe@example.com",
      "isMailIdVerified": true,
      "phoneNumber": "+911234567890",
      "isPhoneNumberVerified": true
}
```

### 6. **DELETE** 

**DELETE**	`/auth/delete`

Deletes the logged-in user's account. Authentication required.

#### Response(200 OK)
*User profile deleted successfully.*
```plaintext
Profile Deleted SuccessFully.
```

### 7. **GENERATE NEW TOKEN** 

**POST**	`/auth/refreshToken`	

Refresh JWT token - returns access and refresh tokens. 
Include the following properties as *body*:

- `refreshToken` - String - Required  

#### Request Body JSON
```json
{
  "refreshToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
}
```
#### Response(200 OK)
*New access and refresh tokens generated successfully and returned as cookies upon valid refresh token submission.*
```json
{
    "status": "Success",
    "message": "Tokens generated",
    "payload": {
        "sub": "<Token Subject>",
        "iat": <Issued at time>,
        "exp": <Expiry time>
    }
}
```
#### Response(401 UNAUTHORIZED)
*Wehn user submits expired or tampered refresh token to get new access and refresh tokens.*
```json
{
    "status": "UNAUTHORIZED",
    "message": "Authentication Failed.",
    "Recovery": "Please login again."
}
```

## Common Validation Error Response

```json
{
    "status": "Bad Request",
    "message": "Validation check failed.",
    "Validation Errors": [
        <Error Messages>
    ]
}
```

**User Registration Validation**
![Validations at User Registration](./screenshots/User%20Validation%20Errors.png)

**Custom Age Validation**
![Age Validation](./screenshots/Validation%20of%20data.png)

</details>


<details>
<summary><strong> 🛒 Product Management Endpoints </strong></summary>
  
### 1. **ADD PRODUCT** 

**POST** `/product/add`


Include the following properties as *body*:

- `productName` - String - Required  
- `category` - String - Required  
- `company` - String - Required  
- `usageLocation` - String - Required  
- `dateOfPurchase` - LocalDate (yyyy-MM-dd) - Required  
- `modeOfPurchase` - ModeOfPurchase - Required
  - modeOfPurchase values: `ONLINE`, `OFFLINE`
- `purchaseSourceName` - String - Required  
- `price` - Double - Required  
- `warrantyPeriodInMonths` - Integer - Required  
- `reminderEnabled` - Boolean - Optional  
- `notes` - String - Optional

#### Request Body
```json
{
  "productName": "LG 43-inch Smart TV",
  "category": "Electronics",
  "company": "LG",
  "usageLocation": "Living Room",
  "dateOfPurchase": "2024-09-15",
  "modeOfPurchase": "ONLINE",
  "purchaseSourceName": "Amazon",
  "price": 34999.99,
  "warrantyPeriodInMonths": 24,
  "reminderEnabled": true,
  "notes": "Mounted on the wall, invoice stored in Drive."
}
```

#### Response(200 OK)
*Product added successfully.*
```json
{
    "status": "Created",
    "message": "Product added successfully.",
    "Recovery": "LG 43-inch Smart TV."
}
```

#### Response(409 CONFLICT)
*Duplicate Product cannot be added.*
```json
{
    "status": "Conflict",
    "message": "Product Already Exists.",
    "Details": "Product with same name under same company and usage location already exists. You are trying to add existing product. If not, try with different product name."
}
```

### 2. **UPDATE PRODUCT** 

**POST** `/product/update`


Include the following properties as *body*:

- `id` - Integer - Required  
- `productName` - String - Required  
- `category` - String - Required  
- `company` - String - Required  
- `usageLocation` - String - Required 
- `price` - Double - Required 
- `reminderEnabled` - Boolean - Optional  
- `notes` - String - Optional

#### Request Body
```json
{
  "id": 120
  "productName": "LG 43-inch Smart TV",
  "category": "Electronics",
  "company": "LG",
  "usageLocation": "Living Room",
  "price": 30000.00,
  "reminderEnabled": false,
  "notes": "Mounted on the wall, invoice stored in Drive. Added extended warranty for a year."
}
```

#### Response(200 OK)
*Product updated successfully.*
```json
{
    "status": "OK",
    "message": "Product Details Updated Successfully.",
    "Details": "LG 43-inch Smart TV"
}
```

#### Response(404 NOT FOUND)
*Try updating others or non-existing products.*
```json
{
    "status": "Not Found",
    "message": "No Products found.",
    "recovery": "Add products by clicking the '+' Button."
}
```

### 3. **VIEW PRODUCTS** 

**GET** `/product/view`


Include the following properties as *query parameters*:

- `page` - Integer - Optional  
- `size` - Integer - Optional
- `sort` - String  - Optional (can be used multiple times for multi-field sorting)

**Default Values(If not provided)**
- `page` : 0  
- `size` : 20
- `sort` : No sorting applied


#### Request URL
```http
GET /product/view?page=0&size=12&sort=productName,desc&sort=price,asc
```

#### Response(200 OK)
*Product fetched successfully.*
```json
{
    "status": "OK",
    "message": "Products fetched Successfully.",
    "Details": {
        "content": [
            {
                "id": 120,
                "productName": "LG 43-inch Smart TV",
                "category": "Electronics",
                "company": "LG",
                "usageLocation": "Living Room",
                "dateOfPurchase": "2024-09-15",
                "modeOfPurchase": "ONLINE",
                "purchaseSourceName": "Amazon",
                "price": 30000.00,
                "warrantyPeriodInMonths": 24,
                "reminderEnabled": false,
                "notes": "Mounted on the wall, invoice stored in Drive. Added extended warranty for a year."
}
        ],
        "pageable": {
            "pageNumber": 0,
            "pageSize": 12,
            "sort": {
                "empty": false,
                "unsorted": false,
                "sorted": true
            },
            "offset": 0,
            "unpaged": false,
            "paged": true
        },
        "totalPages": 1,
        "totalElements": 1,
        "last": true,
        "size": 12,
        "number": 0,
        "sort": {
            "empty": false,
            "unsorted": false,
            "sorted": true
        },
        "numberOfElements": 1,
        "first": true,
        "empty": false
    }
}
```

#### Response(404 NOT FOUND)
*Try fetching products before adding product.*
```json
{
    "status": "Not Found",
    "message": "No Products found.",
    "recovery": "Add products by clicking the '+' Button."
}
```

### 4. **DELETE PRODUCT** 

**POST** `/product/`


Include the following properties as *body*:

- `id` - Integer - Required  

#### Request Body
```json
{
  "id": 120
}
```

#### Response(200 OK)
*Product added successfully.*
```plaintext
Product Deleted Successfully.
```

#### Response(404 NOT FOUND)
*Try deleting others or non-existing product.*
```plaintext
Product Not found.
```

### 5. **FETCH COMPANIES** 

**GET** `/product/companies`


Include the following properties as *query parameters*:

- `page` - Integer - Optional  
- `size` - Integer - Optional
- `sort` - String  - Optional (can be used multiple times for multi-field sorting)

**Default Values(If not provided)**
- `page` : 0  
- `size` : 20
- `sort` : No sorting applied


#### Request URL
```http
GET /product/companies?page=0&size=12&sort=company,desc
```

#### Response(200 OK)
*Distinct companies under which products are registered are fetched successfully.*
```json
{
    "status": "OK",
    "message": "Distinct Categories listed.",
    "Details": {
        "content": [
            "Vivo",
            "Sony",
            "Samsung",
            "Redmi",
            "Philips",
            "MI",
            "Lenovo",
            "JBL",
            "HP",
            "Google",
            "Fossil",
            "Dyson"
        ],
        "pageable": {
            "pageNumber": 0,
            "pageSize": 12,
            "sort": {
                "empty": false,
                "unsorted": false,
                "sorted": true
            },
            "offset": 0,
            "unpaged": false,
            "paged": true
        },
        "totalPages": 2,
        "totalElements": 18,
        "last": false,
        "size": 12,
        "number": 0,
        "sort": {
            "empty": false,
            "unsorted": false,
            "sorted": true
        },
        "numberOfElements": 12,
        "first": true,
        "empty": false
    }
}
```

#### Response(404 NOT FOUND)
*When no products added.*
```json
{
    "status": "Not Found",
    "message": "No Products found. So, no companies found.",
    "Recovery": "Add products by clicking the '+' Button."
}
```

### 6. **FETCH CATEGORIES** 

**GET** `/product/categories`

Include the following properties as *query parameters*:

- `page` - Integer - Optional  
- `size` - Integer - Optional
- `sort` - String  - Optional (can be used multiple times for multi-field sorting)

**Default Values(If not provided)**
- `page` : 0  
- `size` : 20
- `sort` : No sorting applied


#### Request URL
```http
GET /product/categories?page=0&size=12&sort=category,desc
```

#### Response(200 OK)
*Distinct categories under which products are registered are fetched successfully.*
```json
{
    "status": "OK",
    "message": "Distinct Categories listed.",
    "Details": {
        "content": [
            "Wearable",
            "Tablet",
            "Smart Home",
            "Security",
            "Printer",
            "Mobile",
            "Laptop",
            "Headphones",
            "Electronics",
            "Camera",
            "Audio",
            "Appliance"
        ],
        "pageable": {
            "pageNumber": 0,
            "pageSize": 12,
            "sort": {
                "empty": false,
                "sorted": true,
                "unsorted": false
            },
            "offset": 0,
            "paged": true,
            "unpaged": false
        },
        "totalPages": 1,
        "totalElements": 12,
        "last": true,
        "size": 12,
        "number": 0,
        "sort": {
            "empty": false,
            "sorted": true,
            "unsorted": false
        },
        "numberOfElements": 12,
        "first": true,
        "empty": false
    }
}
```

#### Response(404 NOT FOUND)
*When no products added.*
```json
{
    "status": "Not Found",
    "message": "No Products found. So, no categories found.",
    "Recovery": "Add products by clicking the '+' Button."
}
```

### 7. **FETCH USAGE LOCATIONS** 

**GET** `/product/usageLocations`

Include the following properties as *query parameters*:

- `page` - Integer - Optional  
- `size` - Integer - Optional
- `sort` - String  - Optional (can be used multiple times for multi-field sorting)

**Default Values(If not provided)**
- `page` : 0  
- `size` : 20
- `sort` : No sorting applied


#### Request URL
```http
GET /product/usageLocations?page=0&size=12&sort=usageLocation,desc
```

#### Response(200 OK)
*Distinct usage locations under which products are registered are fetched successfully.*
```json
{
    "status": "OK",
    "message": "Distinct usage locations listed.",
    "Details": {
        "content": [
            "Travel",
            "Outdoor",
            "Office",
            "Living Room",
            "Home",
            "Gym"
        ],
        "pageable": {
            "pageNumber": 0,
            "pageSize": 12,
            "sort": {
                "empty": false,
                "sorted": true,
                "unsorted": false
            },
            "offset": 0,
            "paged": true,
            "unpaged": false
        },
        "totalPages": 1,
        "totalElements": 6,
        "last": true,
        "size": 12,
        "number": 0,
        "sort": {
            "empty": false,
            "sorted": true,
            "unsorted": false
        },
        "numberOfElements": 6,
        "first": true,
        "empty": false
    }
}
```

#### Response(404 NOT FOUND)
*When no products added.*
```json
{
    "status": "Not Found",
    "message": "No Products found. So, no usage location found.",
    "Recovery": "Add products by clicking the '+' Button."
}
```

### 8. **PRODUCTS BY CATEGORY** 

**GET** `/product/productsByCategory`

  
Include the following properties as *query parameters*:

 `category` - String - Required

- `page` - Integer - Optional  
- `size` - Integer - Optional
- `sort` - String  - Optional (can be used multiple times for multi-field sorting)

**Default Values(If not provided)**
- `page` : 0  
- `size` : 20
- `sort` : No sorting applied


#### Request URL
```http
GET /product/productsByCategory?category=LaPTOP&page=0&size=12&sort=usageLocation,desc
```

#### Response(200 OK)
*Products by category fetched successfully.*

<details>
<summary>Click to expand</summary>
  
```json
{
    "status": "OK",
    "message": "Products by category fetched successfully.",
    "Details": {
        "content": [
            {
                "id": 4,
                "productName": "Dell XPS 13",
                "category": "Laptop",
                "company": "Dell",
                "usageLocation": "Office",
                "dateOfPurchase": "2024-06-20",
                "modeOfPurchase": "ONLINE",
                "purchaseSourceName": "Dell Official Store",
                "price": 98750.0,
                "warrantyPeriodInMonths": 12,
                "reminderEnabled": false,
                "notes": "Premium Support included."
            },
            {
                "id": 26,
                "productName": "HP Envy x360",
                "category": "Laptop",
                "company": "HP",
                "usageLocation": "Office",
                "dateOfPurchase": "2022-09-22",
                "modeOfPurchase": "OFFLINE",
                "purchaseSourceName": "Reliance Digital",
                "price": 84999.95,
                "warrantyPeriodInMonths": 24,
                "reminderEnabled": true,
                "notes": "Free Office 365 for 1 year."
            },
            {
                "id": 33,
                "productName": "Acer Nitro 5",
                "category": "Laptop",
                "company": "Acer",
                "usageLocation": "Office",
                "dateOfPurchase": "2024-04-12",
                "modeOfPurchase": "ONLINE",
                "purchaseSourceName": "Amazon",
                "price": 65990.0,
                "warrantyPeriodInMonths": 24,
                "reminderEnabled": true,
                "notes": "Ideal for gaming with 16GB RAM. Extended Warranty Applied for a year."
            },
            {
                "id": 30,
                "productName": "Asus ROG Strix G15",
                "category": "Laptop",
                "company": "Asus",
                "usageLocation": "Home",
                "dateOfPurchase": "2024-01-09",
                "modeOfPurchase": "ONLINE",
                "purchaseSourceName": "Flipkart",
                "price": 109990.99,
                "warrantyPeriodInMonths": 24,
                "reminderEnabled": true,
                "notes": "Gaming laptop with RTX 4060."
            }
        ],
        "pageable": {
            "pageNumber": 0,
            "pageSize": 12,
            "sort": {
                "empty": false,
                "sorted": true,
                "unsorted": false
            },
            "offset": 0,
            "paged": true,
            "unpaged": false
        },
        "last": true,
        "totalElements": 4,
        "totalPages": 1,
        "first": true,
        "size": 12,
        "number": 0,
        "sort": {
            "empty": false,
            "sorted": true,
            "unsorted": false
        },
        "numberOfElements": 4,
        "empty": false
    }
}
```
</details>

#### Response(404 NOT FOUND)
*When no products added.*
```json
{
    "status": "Not Found",
    "message": "No Products found under this category.",
    "Recovery": "Add products by clicking the '+' Button."
}
```

### 9. **PRODUCTS BY COMPANY** 

**GET** `/product/productsByCompany`

  
Include the following properties as *query parameters*:

 `company` - String - Required

- `page` - Integer - Optional  
- `size` - Integer - Optional
- `sort` - String  - Optional (can be used multiple times for multi-field sorting)

**Default Values(If not provided)**
- `page` : 0  
- `size` : 20
- `sort` : No sorting applied


#### Request URL
```http
GET /product/productsByCompany?company=applE&page=0&size=12&sort=usageLocation,desc
```

#### Response(200 OK)
*Products by company fetched successfully.*

<details>
<summary>Click to expand</summary>
  
```json
{
    "status": "OK",
    "message": "Products by company fetched successfully.",
    "Details": {
        "content": [
            {
                "id": 25,
                "productName": "Apple iPad Air 5th Gen",
                "category": "Tablet",
                "company": "Apple",
                "usageLocation": "Home",
                "dateOfPurchase": "2023-07-14",
                "modeOfPurchase": "ONLINE",
                "purchaseSourceName": "Apple Store",
                "price": 60990.0,
                "warrantyPeriodInMonths": 12,
                "reminderEnabled": true,
                "notes": "Apple Pencil support available."
            },
            {
                "id": 6,
                "productName": "Apple Watch Series 8",
                "category": "Wearable",
                "company": "Apple",
                "usageLocation": "Gym",
                "dateOfPurchase": "2025-01-12",
                "modeOfPurchase": "ONLINE",
                "purchaseSourceName": "Apple Store",
                "price": 42999.0,
                "warrantyPeriodInMonths": 12,
                "reminderEnabled": true,
                "notes": "Fitness tracking activated."
            }
        ],
        "pageable": {
            "pageNumber": 0,
            "pageSize": 12,
            "sort": {
                "empty": false,
                "sorted": true,
                "unsorted": false
            },
            "offset": 0,
            "paged": true,
            "unpaged": false
        },
        "last": true,
        "totalElements": 2,
        "totalPages": 1,
        "first": true,
        "size": 12,
        "number": 0,
        "sort": {
            "empty": false,
            "sorted": true,
            "unsorted": false
        },
        "numberOfElements": 2,
        "empty": false
    }
}
```
</details>

#### Response(404 NOT FOUND)
*When no products added.*
```json
{
    "status": "Not Found",
    "message": "No Products found under this company.",
    "Recovery": "Add products by clicking the '+' Button."
}
```


### 10. **PRODUCTS BY USAGE LOCATION** 

**GET** `/product/productsByUsageLocation`

  
Include the following properties as *query parameters*:

 `usageLocation` - String - Required

- `page` - Integer - Optional  
- `size` - Integer - Optional
- `sort` - String  - Optional (can be used multiple times for multi-field sorting)

**Default Values(If not provided)**
- `page` : 0  
- `size` : 20
- `sort` : No sorting applied


#### Request URL
```http
GET /product/productsByUsageLocation?usageLocation=HOMe&page=0&size=12&sort=usageLocation,desc
```

#### Response(200 OK)
*Products by usage location fetched successfully.*

<details>
<summary>Click to expand</summary>
  
```json
{
    "status": "OK",
    "message": "Products by company fetched successfully.",
    "Details": {
        "content": [
            {
                "id": 1,
                "productName": "Redmi Note 8",
                "category": "Mobile",
                "company": "Redmi",
                "usageLocation": "Home",
                "dateOfPurchase": "2025-04-27",
                "modeOfPurchase": "ONLINE",
                "purchaseSourceName": "Amazon",
                "price": 8500.0,
                "warrantyPeriodInMonths": 12,
                "reminderEnabled": false,
                "notes": "Free Screen replacement for 1 Year."
            },
            {
                "id": 3,
                "productName": "Samsung Galaxy S21",
                "category": "Mobile",
                "company": "Samsung",
                "usageLocation": "Home",
                "dateOfPurchase": "2023-11-15",
                "modeOfPurchase": "OFFLINE",
                "purchaseSourceName": "Reliance Digital",
                "price": 55999.5,
                "warrantyPeriodInMonths": 24,
                "reminderEnabled": true,
                "notes": "Extended warranty purchased for 2 years."
            },
            {
                "id": 25,
                "productName": "Apple iPad Air 5th Gen",
                "category": "Tablet",
                "company": "Apple",
                "usageLocation": "Home",
                "dateOfPurchase": "2023-07-14",
                "modeOfPurchase": "ONLINE",
                "purchaseSourceName": "Apple Store",
                "price": 60990.0,
                "warrantyPeriodInMonths": 12,
                "reminderEnabled": true,
                "notes": "Apple Pencil support available."
            },
            {
                "id": 27,
                "productName": "Philips Air Fryer HD9252",
                "category": "Appliance",
                "company": "Philips",
                "usageLocation": "Home",
                "dateOfPurchase": "2024-03-05",
                "modeOfPurchase": "ONLINE",
                "purchaseSourceName": "Amazon",
                "price": 8499.0,
                "warrantyPeriodInMonths": 24,
                "reminderEnabled": false,
                "notes": "Low oil healthy frying."
            },
            {
                "id": 30,
                "productName": "Asus ROG Strix G15",
                "category": "Laptop",
                "company": "Asus",
                "usageLocation": "Home",
                "dateOfPurchase": "2024-01-09",
                "modeOfPurchase": "ONLINE",
                "purchaseSourceName": "Flipkart",
                "price": 109990.99,
                "warrantyPeriodInMonths": 24,
                "reminderEnabled": true,
                "notes": "Gaming laptop with RTX 4060."
            },
            {
                "id": 32,
                "productName": "MI 360° Home Security Camera",
                "category": "Security",
                "company": "MI",
                "usageLocation": "Home",
                "dateOfPurchase": "2022-10-08",
                "modeOfPurchase": "ONLINE",
                "purchaseSourceName": "Mi Store",
                "price": 2999.99,
                "warrantyPeriodInMonths": 12,
                "reminderEnabled": false,
                "notes": "Supports night vision and 2-way audio."
            },
            {
                "id": 34,
                "productName": "Google Nest Hub 2nd Gen",
                "category": "Smart Home",
                "company": "Google",
                "usageLocation": "Home",
                "dateOfPurchase": "2023-04-22",
                "modeOfPurchase": "OFFLINE",
                "purchaseSourceName": "Best Buy",
                "price": 7999.0,
                "warrantyPeriodInMonths": 12,
                "reminderEnabled": true,
                "notes": "Smart display with Google Assistant."
            }
        ],
        "pageable": {
            "pageNumber": 0,
            "pageSize": 12,
            "sort": {
                "empty": false,
                "sorted": true,
                "unsorted": false
            },
            "offset": 0,
            "paged": true,
            "unpaged": false
        },
        "last": true,
        "totalElements": 7,
        "totalPages": 1,
        "first": true,
        "size": 12,
        "number": 0,
        "sort": {
            "empty": false,
            "sorted": true,
            "unsorted": false
        },
        "numberOfElements": 7,
        "empty": false
    }
}
```
</details>

#### Response(404 NOT FOUND)
*When no products added.*
```json
{
    "status": "Not Found",
    "message": "No Products found under this usage location.",
    "Recovery": "Add products by clicking the '+' Button."
}
```


### 11. **TURN ON/OFF REMINDER** 

**POST** `/product/switchReminder`


Include the following properties as *body*:

- `id` - Integer - Required
- 
#### Request Body
```json
{
  "id": 120
}
```

#### Response(200 OK)
*Product warranty reminder turened off successfully.*
```json
{
    "status": "OK",
    "message": "Reminder turned off successfully.",
    "Details": "LG 43-inch Smart TV"
}
```

#### Response(200 OK)
*Product warranty reminder turened on successfully.*
```json
{
    "status": "OK",
    "message": "Reminder turned on successfully.",
    "Details": "LG 43-inch Smart TV"
}
```

#### Response(404 NOT FOUND)
*When no products added.*
```json
{
    "status": "Not Found",
    "message": "Product not found.",
    "Recovery": "Add products by clicking the '+' Button."
}
```




</details>
