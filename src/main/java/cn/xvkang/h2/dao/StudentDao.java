package cn.xvkang.h2.dao;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import cn.xvkang.h2.entity.Student;

@Component
public class StudentDao {
	@Autowired
	@Qualifier("h2HibernateSessionFactory")
	private SessionFactory  sessionFactory;

	public void save(Student s) {
		Session openSession = sessionFactory.openSession();
		Transaction beginTransaction = openSession.beginTransaction();
		openSession.save(s);
		beginTransaction.commit();
		openSession.close();
		
	} 
	@SuppressWarnings("unchecked")
	public List<Student> selectAll() {
		Session openSession = sessionFactory.openSession();
		Transaction beginTransaction = openSession.beginTransaction();
		Query createQuery = openSession.createQuery("from Student");
		List<Student> list = createQuery.list();
		beginTransaction.commit();
		openSession.close();
		return list;
		
	} 
	
	
}
