package cn.xvkang.utils.page;
/*
 * Copyright 2008-2014 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

/**
 * Basic Java Bean implementation of {@code Pageable}.
 * 
 * @author Oliver Gierke
 * @author Thomas Darimont
 */
public class PageRequest extends AbstractPageRequest {

	private static final long serialVersionUID = -4541509938956089562L;

	/**
	 * Creates a new {@link PageRequest} with sort parameters applied.
	 * 
	 * @param page
	 *            zero-based page index.
	 * @param size
	 *            the size of the page to be returned.
	 * @param direction
	 *            the direction of the {@link Sort} to be specified, can be
	 *            {@literal null}.
	 * @param properties
	 *            the properties to sort by, must not be {@literal null} or empty.
	 */
	public PageRequest(int page, int size, String... properties) {
		this(page, size);
	}

	/**
	 * Creates a new {@link PageRequest} with sort parameters applied.
	 * 
	 * @param page
	 *            zero-based page index.
	 * @param size
	 *            the size of the page to be returned.
	 * @param sort
	 *            can be {@literal null}.
	 */
	public PageRequest(int page, int size) {
		super(page, size);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.springframework.data.domain.Pageable#getSort()
	 */

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.springframework.data.domain.Pageable#next()
	 */
	public Pageable next() {
		return new PageRequest(getPageNumber() + 1, getPageSize());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.springframework.data.domain.AbstractPageRequest#previous()
	 */
	public PageRequest previous() {
		return getPageNumber() == 0 ? this : new PageRequest(getPageNumber() - 1, getPageSize());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.springframework.data.domain.Pageable#first()
	 */
	public Pageable first() {
		return new PageRequest(0, getPageSize());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(final Object obj) {

		if (this == obj) {
			return true;
		}

		if (!(obj instanceof PageRequest)) {
			return false;
		}

		PageRequest that = (PageRequest) obj;

		return super.equals(that);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return 31 * super.hashCode();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return String.format("Page request [number: %d, size %d, sort: %s]", getPageNumber(), getPageSize());
	}
}
