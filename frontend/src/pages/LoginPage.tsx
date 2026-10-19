import { useState } from "react"
import { useAuth } from "../auth/AuthContext";
import style from  "./LoginPage.module.css"
import { IoIosWallet } from "react-icons/io";
import { PiCoins } from "react-icons/pi";
import { SiAdguard } from "react-icons/si";


export function LoginPage (){

    const {user, login} = useAuth();

    const[form, setForm] = useState({email: '', password:''});
    
    const[loading, setLoading] = useState(false);
    
    const[error, setError] = useState<string | null>(null);


    async function handleLoginSubmit(e : React.SubmitEvent<HTMLFormElement>){

        e.preventDefault();
        setLoading(true);
        setError(null);

        try{
            await login(form);
            
        }catch(err){
            console.error(err)
            setError("Invalid email or password");
        }finally{
            setLoading(false);
        }
    }

    return (

        <div className="container my-4 gap-5 d-flex align-items-center justify-content-center">

            <form onSubmit={handleLoginSubmit} className={`${style.logInForm} gap-4 d-flex flex-column`}>

                <div className={`${style.formTitle}`}>
                    <h2>Welcome back</h2>
                    <small className="subtitle">Log in to see your account, budgets and spending</small>
                </div>
                
                <div className={`${style.emailInput}`}>
                    <label className="form-label" htmlFor="email">Email</label>
                    <input 
                        className="form-control"
                        name="email"
                        id="email"
                        autoComplete="email"
                        onChange={e => setForm({...form, email: e.target.value})}
                        value={form.email}
                        type="email"
                        placeholder="you@example.com"
                        required
                        autoFocus
                        aria-describedby="email-error"
                        disabled={loading}/>
                    
                    <span id="email-error" className="py-2" role="alert"> Email is not valid </span>
                </div>

                <div className={`${style.passwordInput}`}>

                    <div className={`${style.passwordLabel} d-flex justify-content-between`}>
                        <label className="form-label" htmlFor="password">Password</label>
                        <a href="#">Forgot password?</a>
                    </div>
                    <input
                        className="form-control"
                        id="password"
                        name="password"
                        autoComplete="current-password"
                        onChange={e => setForm({...form, password: e.target.value})}
                        type="password"
                        value={form.password}
                        placeholder="Enter your password"
                        disabled={loading}
                        aria-describedby="password-error"
                        required/>

                    <span id="password-error" className="py-2" role="alert">Password is required</span>
                </div>

                <div className={`${style.keepSignInIinput} d-flex gap-2`}>
                    <input type="checkbox" id="keep-sign-in" name="keep-sign-in"/>
                    <label htmlFor="keep-sign-in">Keep me signed in on this device</label>
                </div>

                <button className="btn btn-success" type="submit" disabled={loading}>
                    {loading ? "logging in..." : "Log in"}
                </button>
                
                <div className={style.divider}>
                  <span>or</span>
                </div>

                <div className={style.signUp}>
                    <span>Don't have an account?</span> <a href="#">Sign up for free</a>
                </div>

                {error && <p>{error}</p>}
                {user?.username && <p>Logged in as {user?.username}</p>}
            </form>

            <div className={`${style.logInInfo}`}>

                <p>Personal Finance, made clear</p>

                <h1>
                    Every account, budget and goal in one calm view.
                </h1>

                <div className={`${style.logInInfoItemGroup} d-flex flex-column gap-3`}>

                    <div className={style.info}>

                        <div className={style.infoIcon}>
                            <IoIosWallet />
                        </div>

                        <div className={style.infoDescription}>
                            <strong>All your accounts together</strong>
                            <small>Cards, savings and cash, tracked side by side</small>
                        </div>
                    </div>
                    <div className={style.info}>

                        <div className={style.infoIcon}>
                            <PiCoins />
                        </div>

                        <div className={style.infoDescription}>
                            <strong>Budgets that keep up</strong>
                            <small>See what's left to spend before the month ends</small>
                        </div>
                    </div>
                    <div className={style.info}>

                        <div className={style.infoIcon}>
                            <SiAdguard />
                        </div>

                        <div className={style.infoDescription}>
                            <strong>Private by default</strong>
                            <small>Your data is encrypted and never sold</small>
                        </div>
                    </div>

                </div>
            </div>
        </div>
    )
}