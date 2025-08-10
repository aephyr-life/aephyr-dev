# ADR-0001: Use Magic Links for User Authentication

**Date:** 2025-08-08  
**Status:** Accepted

## Context
We need an authentication mechanism for Aephyr’s first version. 
The primary audience is non-technical users who may log in infrequently. 
Passwords introduce usability friction (creation, reset flows) 
and security risks (reuse, weak passwords, phishing).
However, for long-term security and UX improvements, 
we want to support passwordless login using WebAuthn.

## Decision
We will use magic links sent via email as the sole login method.
- User enters their email address.
- The backend generates a single-use, time-limited token and sends it as part of a login URL.
- Clicking the link authenticates the user and starts their session.
We will design our authentication domain so that adding WebAuthn later will not require
breaking changes to our user model or session handling.

## Consequences
**Pros:**
- Very low user friction — no password to remember.
- Reduces risk from password reuse and credential leaks.
- Aligns with modern passwordless trends (Slack, Medium, etc.).
- Simplifies UX and onboarding.

**Cons:**
- Hard dependency on email deliverability.
- Login blocked if user cannot access email.
- Potential phishing vector (must educate users to check sender).
- Session hijacking risk if email account is compromised.

## Alternatives Considered
- Username/password — higher friction, password resets needed, higher security risk from poor passwords.
- OAuth (Google, Apple, etc.) — better UX for some, but requires provider accounts and adds complexity early on.
- WebAuthn — strongest security, but high initial complexity and device dependency.

## Notes
- All tokens expire after 15 min and are single-use.
- We will log IP and User-Agent for link use, and consider device binding later.
- Switching away from magic links in the future will require significant user and system changes.
