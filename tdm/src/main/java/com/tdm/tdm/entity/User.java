package com.tdm.tdm.entity;

import javax.persistence.*;
import java.util.Collection;

@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = "email"))
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String userName;
    private String email;
    private String password;
    private String accountName;
    
    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(
            name = "users_roles",
            joinColumns = @JoinColumn(
                    name = "user_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(
                    name = "role_id", referencedColumnName = "id"))
    private Collection<UserRole> roles;

    public User() {
    }

    public User(String userName,  String email, String password,String accountName) {
        this.userName = userName;
        
        this.email = email;
        this.password = password;
        this.accountName =accountName;
    }

    public User(String userName,  String email, String password, String accountName,Collection<UserRole> roles) {
        this.userName = userName;
        this.accountName =accountName;
        this.email = email;
        this.password = password;
        this.roles = roles;
    }

        
	public String getaccountName() {
		return accountName;
	}

	public void setaccountName(String accountName) {
		this.accountName = accountName;
	}

	public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getuserName() {
        return userName;
    }

    public void setuserName(String userName) {
        this.userName = userName;
    }

  
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Collection<UserRole> getRoles() {
        return roles;
    }

    public void setRoles(Collection<UserRole> roles) {
        this.roles = roles;
    }

	@Override
	public String toString() {
		return "User [id=" + id + ", userName=" + userName + ", email=" + email + ", password=" + password + ", accountName="
				+ accountName + ", roles=" + roles + "]";
	}

   
}
