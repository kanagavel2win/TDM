package com.tdm.tdm.entity;

import javax.persistence.*;
import java.util.Collection;

@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = "email"))
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String memberID;
    private String email;
    private String password;
    private String clubID;
    
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

    public User(String memberID,  String email, String password,String clubID) {
        this.memberID = memberID;
        
        this.email = email;
        this.password = password;
        this.clubID =clubID;
    }

    public User(String memberID,  String email, String password, String clubID,Collection<UserRole> roles) {
        this.memberID = memberID;
        this.clubID =clubID;
        this.email = email;
        this.password = password;
        this.roles = roles;
    }

        
	public String getClubID() {
		return clubID;
	}

	public void setClubID(String clubID) {
		this.clubID = clubID;
	}

	public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getmemberID() {
        return memberID;
    }

    public void setmemberID(String memberID) {
        this.memberID = memberID;
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
		return "User [id=" + id + ", memberID=" + memberID + ", email=" + email + ", password=" + password + ", clubID="
				+ clubID + ", roles=" + roles + "]";
	}

   
}
