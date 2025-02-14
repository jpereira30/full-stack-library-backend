# Library API

A backend service for managing books and generating AI-powered insights about books. This API provides endpoints for book management and interacting with OpenAI's GPT model for generating book-related insights.

## 🚀 **Features**
- **Book Management**: Create, retrieve, update, and delete books.
- **AI Insights**: Generate AI-powered insights and taglines for books using OpenAI’s API.

---

## 🛠 **Technologies Used**
- **Spring Boot**: Java-based framework to build the backend.
- **Spring WebFlux**: Reactive programming with WebClient for making API calls.
- **Lombok**: To reduce boilerplate code (e.g., getters, setters, `toString`, etc.).
- **OpenAI API**: To generate insights for books using the GPT models.
- **JUnit & Mockito**: For unit testing and mocking external services.
- **Spotless**: For maintaining code formatting consistency.

---

## 📝 **Installation and Setup**

### 1. Clone the Repository
First, clone the repository to your local machine:

```bash
git clone https://github.com/your-username/library-api.git
```
## 🚀 2. Install Dependencies

Navigate to the project directory and install the dependencies using Maven:

```bash
cd library-api
mvn spotless:apply clean install
```

## 🔑 3. Set Environment Variables

Create a `.env` file in the root directory and add the following:

```plaintext
OPENAI_API_URL=https://api.openai.com
OPENAI_API_KEY=your_openai_api_key
OPENAI_MODEL=gpt-3.5-turbo
```

## 💳 4. Set Up Billing in OpenAI

To use the OpenAI API, you must have a valid OpenAI account with API keys and billing setup.

1. **Create an OpenAI account**: Visit [OpenAI's website](https://www.openai.com).
2. **Set up billing**: After signing up, visit [OpenAI Billing](https://platform.openai.com/account/billing) to enter your payment details.
3. **Obtain an API key**: Go to [API Keys](https://platform.openai.com/account/api-keys) and generate a new key. Paste it into the `OPENAI_API_KEY` field in the `.env` file.


# 🚀 5. Run the Application

Once the environment variables are set, you can start the application with:

```bash
mvn spring-boot:run
