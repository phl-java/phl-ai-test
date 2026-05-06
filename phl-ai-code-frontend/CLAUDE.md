# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

This is an AI code generator frontend application built with Vue 3 + TypeScript + Ant Design Vue. The application allows users to create website applications through AI conversation, view generated website effects, deploy applications, and manage personal applications.

## Architecture & Structure

- **Frontend Framework**: Vue 3 + TypeScript
- **UI Components**: Ant Design Vue
- **State Management**: Pinia
- **Routing**: Vue Router 4
- **Build Tool**: Vite
- **API Client**: Axios

### Directory Structure
```
src/
├── api/                    # API interface definitions
│   ├── appController.ts    # Application related APIs
│   ├── userController.ts   # User related APIs
│   └── typings.d.ts        # Type definitions
├── components/             # Reusable components
│   ├── GlobalHeader.vue    # Global header
│   └── GlobalFooter.vue    # Global footer
├── layouts/                # Layout components
│   └── BasicLayout.vue     # Basic layout
├── pages/                  # Page components
│   ├── HomePage.vue        # Homepage
│   ├── app/                # Application pages
│   │   ├── AppChatPage.vue # App conversation page
│   │   └── AppEditPage.vue # App edit page
│   ├── admin/              # Admin pages
│   │   ├── AppManagePage.vue   # App management page
│   │   ├── UserManagePage.vue  # User management page
│   │   └── ChatManagePage.vue  # Chat management page
│   └── user/               # User pages
│       ├── UserLoginPage.vue
│       └── UserRegisterPage.vue
├── stores/                 # State management
│   └── loginUser.ts        # Login user state
├── utils/                  # Utility functions
│   ├── constants.ts        # Constant definitions
│   ├── format.ts           # Formatting utilities
│   └── validation.ts       # Validation utilities
├── router/                 # Routing configuration
│   └── index.ts
├── assets/                 # Static assets
│   └── images/            # Image files
└── main.ts                 # Application entry

Configuration Files:
├── vite.config.ts          # Vite configuration with proxy setup
├── .env.development        # Development environment variables
├── .env.production         # Production environment variables
└── tsconfig.json          # TypeScript configuration
```

## Key Features

- **Application Creation**: Input user prompts to create applications
- **AI Conversation**: Generate website applications through AI conversation with real-time preview
- **Application Management**: Modify application information (name) and delete applications
- **Application Deployment**: Deploy applications to cloud
- **Admin Functions**: Manage all applications, edit information, set featured status
- **Chat History**: Persistent conversation history for each application
- **Code Download**: Download generated source code
- **Real-time Preview**: Live preview of generated applications during conversation

## Important Files & Patterns

- Authentication is handled through the `loginUser` store with role-based access control
- Routes are defined in `src/router/index.ts` (admin routes need manual role checking)
- API calls follow a consistent pattern using auto-generated controllers from OpenAPI specs
- Components are organized in `src/components/` for reusability across pages
- Environment variables are configured in `.env.*` files
- Vite configuration includes proxy setup for API requests
- Path aliases: `@` maps to `src/` directory
- Markdown rendering with syntax highlighting for chat messages

## Common Commands

- `npm install`: Install dependencies
- `npm run dev`: Start development server (with proxy to backend at http://localhost:8123)
- `npm run build`: Build production version (runs type-check + build)
- `npm run pure-build`: Build without type checking (faster)
- `npm run openapi2ts`: Generate TypeScript types from OpenAPI spec
- `npm run type-check`: Run TypeScript compiler for type checking
- `npm run lint`: Check and fix ESLint code style issues
- `npm run format`: Format code using Prettier
- `npm run preview`: Preview built application locally
- `npm run build-only`: Build production version without type checking

## Development Guidelines

- Use TypeScript consistently across all files
- Leverage Ant Design Vue components for UI consistency
- Implement proper error handling for API calls
- Follow Vue 3 composition API patterns (setup script syntax)
- Use Pinia for state management
- Maintain responsive design across different screen sizes
- Use environment variables for configuration
- API response handling: always check `code === 0` for success
- Use auto-generated API controllers instead of manual axios calls
- Follow existing component patterns with template + script + style structure

## API Integration

- Backend API is proxied during development (http://localhost:8123)
- API endpoints are automatically generated from OpenAPI specifications
- All API calls return consistent response format with code, data, and message
- Error handling should check for response codes (0 indicates success)
- BaseResponse types are defined in `src/api/typings.d.ts`
- API controllers are auto-generated and should not be manually modified
- Run `npm run openapi2ts` to regenerate API types after backend changes

## Project-Specific Notes

- **User Roles**: System supports different user roles (admin vs regular users)
- **App Ownership**: Users can only modify/delete their own applications unless admin
- **Deployment**: Applications are deployed with unique deploy keys for static hosting
- **Chat Streaming**: Real-time chat uses Server-Sent Events (SSE) for streaming responses
- **Code Generation**: Multiple code generation types supported (configurable per app)
- **Static Resources**: Deployed apps served as static resources via deploy keys