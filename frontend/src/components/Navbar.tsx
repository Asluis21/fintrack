import { useAuth } from '../auth/AuthContext';
import styles from './Navbar.module.css';
import { Navbar as BsNavbar, Nav, Container } from 'react-bootstrap';

export function Navbar () {

    const {user, isLoading, logout} = useAuth();

    async function handleLogout() {
        try{
            await logout();
        } catch (err){
            console.log(err);
        }
    }

    let authArea : React.ReactNode;

    if(isLoading){
        authArea = null;
    } else if (user) {

        authArea = (
            <div className="d-flex align-items-center gap-3">
                <span>{user.username}</span>
                <button type="button" className="btn btn-outline-secondary btn-sm" onClick={handleLogout}>
                    Log out
                </button>
            </div>
        );

    } else {

        authArea = (
            <div className={`d-flex ${styles.btnAccess, styles.authArea} align-items-center gap-3 `}>
                <a href="#" className="text-decoration-none d-none d-xl-inline">New to FinTrack?</a>
                <button className={`btn ${styles.btnCreateAccount} my-3 my-md-0`}>Create an account</button>
            </div>
        )
    }

    return (
        <BsNavbar expand="lg" collapseOnSelect>
              
            <div className="container-fluid px-5" style={{background: 'var(--navbar)'}}>

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

                {/* md and below only: CTA always visible */}
                {!isLoading && !user && (
                    <button className={`btn btn-sm ${styles.btnCreateAccount} d-none d-md-inline-block d-lg-none ms-auto me-2`}>
                        Sign up
                    </button>
                )}

                <BsNavbar.Toggle aria-controls="main-nav" style={{background: 'var(--accent)'}}/>

                <BsNavbar.Collapse id="main-nav">
                    <Nav className="mx-auto gap-lg-4">
                        <Nav.Link href="#features">Features</Nav.Link>
                        <Nav.Link href="#pricing">Pricing</Nav.Link>
                        <Nav.Link href="#security">Security</Nav.Link>
                        <Nav.Link href="#help">Help</Nav.Link>
                    </Nav>
                    {authArea}
                </BsNavbar.Collapse>
            </div>
        </BsNavbar>
    )
}