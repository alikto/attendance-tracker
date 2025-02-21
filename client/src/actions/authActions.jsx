
// Action Types
export const LOGIN_SUCCESS = "LOGIN_SUCCESS";
export const LOGIN_FAIL = "LOGIN_FAIL";
export const LOGOUT = "LOGOUT";

// Login Action
export const login = (email, password) => async (dispatch) => {
    try {
        const response = await fetch("http://localhost:8080/auth/login", {
            method: "POST",
            headers: {
                "Content-Type": "application/json",
            },
            body: JSON.stringify({ email, password }),
        });

        if (!response.ok) {
            throw new Error("Login failed");
        }

        const data = await response.json(); // ✅ Properly parse JSON
        console.log("Login Response:", data);

        const { token, userDTO } = data; // ✅ Extract correct fields

        // ✅ Store in localStorage
        localStorage.setItem("token", token);
        localStorage.setItem("user", JSON.stringify(userDTO));

        console.log("Stored User:", JSON.stringify(userDTO));

        dispatch({
            type: LOGIN_SUCCESS,
            payload: { token, user: userDTO },
        });

    } catch (error) {
        console.error("Login Error:", error);
        dispatch({ 
            type: LOGIN_FAIL, 
            payload: error.message || "Login failed" 
        });
    }
};


// Logout Action
export const logout = () => {
    localStorage.removeItem("token");
    localStorage.removeItem("user");
    
    return { type: LOGOUT };
};
