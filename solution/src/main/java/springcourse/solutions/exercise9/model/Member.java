package springcourse.solutions.exercise9.model;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.UUID;

/**
 * @author Amit Tal
 * @since 3/24/14
 */
public class Member implements Serializable {

    private String firstName;
    private String lastName;
    private String memberId;
    private String email;
    private Collection<Book> loanedBooks;

    protected Member() {
        this.memberId = UUID.randomUUID().toString();
        this.loanedBooks = new ArrayList<Book>();
    }

    public Member(String firstName, String lastName, String memberId, String email) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        if (memberId != null) {
            this.memberId = memberId;
        } else {
            this.memberId = UUID.randomUUID().toString();
        }
        this.loanedBooks = new ArrayList<Book>();
    }

    public Member(String firstName, String lastName, String email) {
        this(firstName, lastName, null, email);
    }

    @NotBlank
    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getMemberId() {
        return memberId;
    }

    @Email
    public String getEmail() {
        return email;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Collection<Book> getLoanedBooks() {
        return loanedBooks;
    }

    public void setLoanedBooks(Collection<Book> loanedBooks) {
        this.loanedBooks = loanedBooks;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Member member = (Member) o;

        if (loanedBooks != null ? !loanedBooks.equals(member.loanedBooks) : member.loanedBooks != null) return false;
        if (email != null ? !email.equals(member.email) : member.email != null) return false;
        if (firstName != null ? !firstName.equals(member.firstName) : member.firstName != null) return false;
        if (lastName != null ? !lastName.equals(member.lastName) : member.lastName != null) return false;
        if (memberId != null ? !memberId.equals(member.memberId) : member.memberId != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = firstName != null ? firstName.hashCode() : 0;
        result = 31 * result + (lastName != null ? lastName.hashCode() : 0);
        result = 31 * result + (memberId != null ? memberId.hashCode() : 0);
        result = 31 * result + (email != null ? email.hashCode() : 0);
        result = 31 * result + (loanedBooks != null ? loanedBooks.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Member{");
        sb.append("firstName='").append(firstName).append('\'');
        sb.append(", lastName='").append(lastName).append('\'');
        sb.append(", email='").append(email).append('\'');
        sb.append(", memberId='").append(memberId).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
