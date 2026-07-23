/* ==========================================================================
   Global Interactions (static/js/main.js)
   ========================================================================== */

document.addEventListener('DOMContentLoaded', () => {
    
    // Sticky Navbar Background on Scroll
    const navbar = document.getElementById('mainNavbar');
    
    window.addEventListener('scroll', () => {
        if (window.scrollY > 50) {
            navbar.classList.add('scrolled');
        } else {
            navbar.classList.remove('scrolled');
        }
    });

    // Initialize Bootstrap Tooltips & Popovers if added later
    const tooltipTriggerList = document.querySelectorAll('[data-bs-toggle="tooltip"]');
    const tooltipList = [...tooltipTriggerList].map(tooltipTriggerEl => new bootstrap.Tooltip(tooltipTriggerEl));
});