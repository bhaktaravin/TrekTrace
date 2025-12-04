# TrekTrace Backend

Track your travel adventures with TrekTrace.

## Prerequisites

- Java 21
- Maven
- MongoDB Atlas account (or local MongoDB)
- Appwrite account

## Setup

1. **Clone the repository**
   ```bash
   git clone <repository-url>
   cd demo
   ```

2. **Configure environment variables**
   
   Copy the example environment file:
   ```bash
   cp .env.example .env
   ```
   
   Update `.env` with your actual credentials:
   - `MONGODB_URI`: Your MongoDB connection string
   - `MONGODB_DATABASE`: Database name (default: travel)
   - `JWT_SECRET`: A secure random string for JWT signing
   - `APPWRITE_PROJECT_ID`: Your Appwrite project ID
   - `APPWRITE_API_KEY`: Your Appwrite API key
   - `APPWRITE_ENDPOINT`: Appwrite endpoint (default: https://cloud.appwrite.io/v1)

3. **Run the application**
   ```bash
   ./mvnw spring-boot:run
   ```

## API Endpoints

### Authentication
- `POST /auth/register` - Register a new user
- `POST /auth/login` - Login and get JWT token

### Trips
- `POST /trips` - Create a new trip with photos

## Security

⚠️ **Important**: Never commit the `.env` file to version control. It contains sensitive credentials.

## Development

The application uses:
- Spring Boot 4.0.0
- MongoDB for data storage
- JWT for authentication
- Appwrite for photo storage
- Lombok for reducing boilerplate

## License

MIT
