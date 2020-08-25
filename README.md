# Auth Management Service

This is a service application for managing authentications through the SSO OAuth2 Server.

## Database Setup

First, the application must be registered as a client with the SSO OAuth2 Auth Server. Fill in the question marks.

```
INSERT INTO clients (name, client_key, client_secret, enabled, access_token_timeout_secs, refresh_token_timeout_secs, auth_code_timeout_secs, redirect_uri)
VALUES ('auth-management-service', ?, ?, true, ?, ?, ?, ?);
```

Second, a user must be added who can access this app. The password must be BCrypt encrypted. The user must be added as a part of this client.

```
INSERT INTO users (email, first_name, last_name, password, enabled)
VALUES (?, ?, ?, true);

INSERT INTO client_users (user_id, client_id)
VALUES (?, ?);
```

## Running Locally

Use the run script, not the normal maven command:

```
sh run.sh
```
