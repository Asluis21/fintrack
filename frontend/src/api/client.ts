const API_BASE_URL = 
    import.meta.env.VITE_BACKEND_BASE_URL ?? "http://localhost:8080";

export function apiFetch(
    path:string, 
    options: RequestInit = {}
) : Promise<Response>{

    const url = `${API_BASE_URL.replace(/\/$/, "")}/${path.replace(/^\//, "")}`;
    const token = sessionStorage.getItem("jwt-token");
    
    if(!token){
        return fetch(url, {
        ...options,
        headers: {
            "Content-Type":"application/json",
            ...options.headers,
        },
    });    
    }

    return fetch(url, {
        ...options,
        headers: {
            "Content-Type":"application/json",
            // Then I will change it to cookie instead of session storage
            "Authorization": `Bearer ${sessionStorage.getItem("jwt-token")}`,
            ...options.headers,
        },
    });
    
}