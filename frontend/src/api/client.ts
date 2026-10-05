const API_BASE_URL = 
    import.meta.env.VITE_BACKEND_BASE_URL ?? "http://localhost:8080";

export function apiFetch(
    path:string, 
    options: RequestInit = {}
) : Promise<Response>{

    const url = `${API_BASE_URL.replace(/\/$/, "")}/${path.replace(/^\//, "")}`;
    
    return fetch(url, {
        ...options,
        credentials: "include",
        headers: {
            "Content-Type":"application/json",
            ...options.headers,
        },
    })
}