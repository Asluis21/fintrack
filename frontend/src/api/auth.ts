import { apiFetch } from "./client";

export interface LoginUserRequest {
    email : string;
    password : string;
}

export interface LoginResponse {
    token : string;
}

export interface User {
    username : string;
}

export async function login(user: LoginUserRequest) : Promise<void>{
    
    const response = await apiFetch("/auth/login", {
        method: "POST",
        body: JSON.stringify(user),
    });

    if(!response.ok){
        throw new Error('Failed authentication');
    }
}

export async function getCurrentUser() : Promise<User | null> {
    
    const response = await apiFetch("/auth/me");

    if(response.status === 401) return null;
    if(!response.ok) throw new Error("Failed to load user");
    return response.json();
}

export async function logout() : Promise<void>{
    await apiFetch("/auth/logout", {method: "POST"});
}