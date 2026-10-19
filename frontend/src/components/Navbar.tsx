import { useAuth } from '../auth/AuthContext';
import styles from './Navbar.module.css';

export function Navbar () {

    const {user} = useAuth();

    return (
        <nav className={`navbar navbar-expand-lg d-flex justify-content-between`}>            

            <div className="container">

                <div className={styles.pageLogo}>
                    <div className="image">
                        <svg width="32" height="32" viewBox="0 0 32 32" aria-hidden="true">
                            <rect width="32" height="32" rx="8" fill="#0F1A16"/>
                            <path d="M8 21l5-5 4 3 7-8" fill="none" stroke="#B6E36B" strokeWidth="2.5" strokeLinecap="round" strokeLinejoin="round"/>
                        </svg>
                    </div>

                    <div className="title">
                        <strong>FinTrack</strong>
                    </div>
                </div>


                <div className={`${styles.featureOptions} gap-5 d-flex`}>
                    <a href="#" className="text-decoration-none option-1">Features</a>
                    <a href="#" className="text-decoration-none option-2">Pricing</a>
                    <a href="#" className="text-decoration-none option-3">Security</a>
                    <a href="#" className="text-decoration-none option-4">Help</a>
                </div>

                

                {!user ? (
                    <div className={`d-flex ${styles.btnAccess} align-items-center gap-3`}>
                        <a href="#" className="text-decoration-none">New to FinTrack?</a>
                        <button className={`btn ${styles.btnCreateAccount}`}>Create an account</button>
                    </div>
                ) : (

                    <a href="#">Log out</a>
                )}
            </div>
        </nav>
    )
}