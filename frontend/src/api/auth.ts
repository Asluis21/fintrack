import { apiFetch } from "./client";

export interface LoginUserRequest {
    email : string;
    password : string;
}

export interface LoginResponse {
    token : string;
}

export async function login(
    user: LoginUserRequest
) : Promise<LoginResponse>{
    const response = await apiFetch("/auth/login", {
        method: "POST",
        body: JSON.stringify(user),
    });

    if(!response.ok){
        throw new Error('Failed authentication');
    }

    return response.json();
}