package mock.claimrequest.entity;

import java.sql.Date;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Entity
@Table(name="employees")
public class Employee {

    @Id
    @GeneratedValue(strategy=GenerationType.UUID) 
    private UUID id ; 
    
    @Column(name="first_name" )
    private String firstName;
    
    @Column(name="last_name" )
    private String lastName; 

    @Column(name="gender" )
    private boolean gender ; 
    
    @Column(name="date_of_birth" )
    private Date dob ; 
    
    @Column(name="address" )
    private String address ; 
    
    @Column(name="ranked" )
    private String ranked ;
    
    @Column(name="salary" )
    private String salary ;
}
