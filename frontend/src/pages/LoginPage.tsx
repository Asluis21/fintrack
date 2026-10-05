import { useState } from "react"
import { login } from "../api/auth";

export function LoginPage (){
    
    const[form, setForm] = useState({email: '', password:''});
    
    const[loading, setLoading] = useState(false);
    
    const[error, setError] = useState<string | null>(null);

    const[loggedInAs, setLoggedInAs] = useState<string | null>(null);

    async function handleLoginSubmit(e : React.SubmitEvent<HTMLFormElement>){

        e.preventDefault();
        setLoading(true);
        setError(null);

        try{
            await login(form);
            setLoggedInAs(form.email);
            
        }catch(err){
            console.error(err)
            setError("Invalid email or password");
        }finally{
            setLoading(false);
        }
    }

    return (

        <form onSubmit={handleLoginSubmit}>
            <input 
                name="email" 
                onChange={e => setForm({...form, email: e.target.value})} 
                value={form.email}
                type="email" 
                placeholder="Email"/>
            
            <input 
                name="password" 
                onChange={e => setForm({...form, password: e.target.value})} 
                type="password" 
                value={form.password}
                placeholder="Password"/>

            <button type="submit" disabled={loading}>
                {loading ? "logging in..." : "Log in"}
            </button>
            
            {error && <p>{error}</p>}
            {loggedInAs && <p>Logged in as {loggedInAs}</p>}
        </form>
    )
}