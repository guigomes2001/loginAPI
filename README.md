🔐 LoginAPI

LoginAPI is a secure and scalable authentication and user management API built with Spring Boot 3, JWT, and MySQL.
It provides endpoints for login, logout, password recovery, session renewal, token validation, user registration, and more.

Developed to serve as an independent authentication microservice for web or mobile systems.

🚀 Features

- ✅ User authentication with encrypted passwords (JWT-based)
- 🔄 Session renewal and logout
- 🆕 User registration and reactivation
- 🔒 Password recovery and reset via email
- 🧩 Token management and validation
- ⚙️ Secure HTTPS configuration with SSL
- 🧠 Modular architecture (BO, DAO, DTO, Repository, Security, Util)

  <img width="475" height="649" alt="image" src="https://github.com/user-attachments/assets/4f5482bc-f5ec-44a1-9a4e-79d618c66e20" />

⚙️ Technologies Used

- Java 21
- Spring Boot 3.5.6
- Spring Security
- Spring Data JPA
- JWT (io.jsonwebtoken 0.11.5)
- Spring Mail
- MySQL
- Maven
- Tomcat (WAR packaging)
- HTTPS (SSL with PKCS12)

📦 Installation

1. Clone the repository
git clone https://github.com/yourusername/loginAPI.git
cd loginAPI

2. Configure the database

Create a MySQL database (for example: criptocom_teste):
CREATE DATABASE criptocom_teste;

Then, update your application.properties file:

- spring.datasource.url=jdbc:mysql://localhost:3306/criptocom_teste?useSSL=false&serverTimezone=UTC
- spring.datasource.username=root
- spring.datasource.password=YOUR_PASSWORD

3. Configure email (for password recovery)

Edit the Gmail credentials in application.properties:

- spring.mail.username=your.email@gmail.com
- spring.mail.password=your_app_password

(Use an App Password if using Gmail with 2FA)

4. Build the project
mvn clean install

5. Run the API
mvn spring-boot:run

The API will start at:
🔗 https://localhost:8443/loginAPI

🔑 API Endpoints

 *Method	   *Endpoint	      *Description
- POST	/loginAPI/logar	-- Authenticate user
- POST	/loginAPI/logout --	Logout user and invalidate session
- POST	/loginAPI/renovarSessao --	Renew user session via token
- POST	/loginAPI/cadastrar	-- Register a new user
- DELETE	/loginAPI/excluirUsuario/{id}	-- Delete user
- PUT	/loginAPI/bloquearUsuario/{id}	-- Block user
- PUT	/loginAPI/alterarSenha --	Change user password
- POST	/loginAPI/recuperarSenha --	Send password recovery token
- POST	/loginAPI/redefinirSenha	-- Reset password using token
- POST	/loginAPI/reativarUsuario	-- Reactivate user account
- POST	/token/validarToken --	Validate JWT token
POST	/token/reenviarTokenRecuperacaoDeSenha	-- Resend password recovery token

🧠 Example Request
🔹 Login
POST /loginAPI/logar
{
  "login": "user@example.com",
  "senha": "MyPassword123"
}

🔹 Response
{
  "success": true,
  "error": false,
  "message": "Login realizado com sucesso",
  "data": {
    "id": 1,
    "login": "user@example.com",
    "token": "eyJhbGciOiJIUzI1NiIsInR5..."
  }
}

🧰 Maven Configuration

If you want to rebuild or modify dependencies, use:
mvn dependency:tree

🔐 Security and HTTPS

The project is pre-configured to use HTTPS on port 8443.
The SSL keystore (keystore.p12) must be placed in src/main/resources/.

Example configuration in application.properties:

- server.ssl.key-store=classpath:keystore.p12
- server.ssl.key-store-password=APiCr1ptoHTTP$
- server.ssl.key-store-type=PKCS12
- server.ssl.key-alias=alia

🧑‍💻 Author

Guilherme Gomes
📧 criptocomofficial@gmail.com

💼 @gui_gomes_18

📝 License

This API is proprietary software.
Unauthorized redistribution, modification, or resale is prohibited without the author’s permission.
