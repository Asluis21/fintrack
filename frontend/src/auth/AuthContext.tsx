import React, { createContext, useContext, useEffect, useState } from "react";
import * as authApi from "../api/auth";
import { getCurrentUser, login, logout, type LoginUserRequest, type User } from "../api/auth"

type AuthContextValue = {
    user: User | null;
    isLoading: boolean;
    login: (credentials : LoginUserRequest) => Promise<void>;
    logout : () => Promise<void>
}

const AuthContext = createContext<AuthContextValue | null>(null);

export function AuthProvider({children} : {children : React.ReactNode}){

    const [user, setUser] = useState<User | null>(null);
    const [isLoading, setIsLoading] = useState(true);

    useEffect(() => {
        getCurrentUser()
            .then(setUser)
            .catch(() => setUser(null))
            .finally(() => setIsLoading(false))
    }, [])

    async function login(credentials:LoginUserRequest) {
        await authApi.login(credentials);
        const me = await getCurrentUser();
        setUser(me);
    }

    async function logout(){
        await authApi.logout();
        setUser(null);
    }

    return (
        <AuthContext.Provider value={{user, isLoading, login, logout}}>
            {children}
        </AuthContext.Provider>
    );
}

export function useAuth(){
    const ctx = useContext(AuthContext);
    if(!ctx) {
        throw new Error("useAuth must be used inside <AuthProvider>");
    }
    return ctx;
}

