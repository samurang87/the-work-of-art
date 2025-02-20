# 🎨 The Work of Art 🎨

A full-stack application for artists to showcase their work, share techniques, and participate in creative challenges.
This is my Capstone project for the Neue Fische Java Fullstack course.

## Features
- GitHub authentication
- Upload and manage artwork
- Material tracking

Upcoming:
- Filter by medium
- Art challenges

## Tech Stack

### Backend
- Kotlin with Spring Boot
- MongoDB Atlas
- Cloudinary for image storage
- GitHub OAuth for authentication

### Frontend
- React + TypeScript
- Tailwind CSS

### Deployment
- Github Actions
- Docker
- Render

## Setup Guide

### 1. MongoDB Atlas
1. Visit [MongoDB Atlas](https://www.mongodb.com/cloud/atlas)
2. Create a free cluster
3. Set up database access:
   - Username: your_username
   - Password: generate a secure password
4. Add your IP to Network Access
5. Get your connection string

### 2. Cloudinary
1. Sign up at [Cloudinary](https://cloudinary.com/)
2. From your dashboard, collect:
   - Cloud Name
   - API Key
   - API Secret

### 3. GitHub OAuth
1. Go to GitHub Settings > Developer settings > OAuth Apps
2. Create a new OAuth app:
   - Homepage URL: `http://localhost:8080`
   - Authorization callback URL: `http://localhost:8080/login/oauth2/code/github`
3. Save the Client ID and Client Secret

### 4. Run the Application

Backend: run BackendApplication in your IDE. Set environment variables according to `backend/src/main/resources/application.properties`.

Frontend:
```bash
cd frontend
npm install
npm run dev
```

Visit http://localhost:5173 and log in with your GitHub account. If running with Docker, the application will be available at http://localhost:8080.
