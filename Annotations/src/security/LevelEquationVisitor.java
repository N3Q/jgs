package security;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import security.LevelEquation.Level;
import security.LevelEquation.LevelMaxEquation;
import security.LevelEquation.LevelMinEquation;

/**
 * 
 * @author Thomas Vogel
 * @version 0.1
 */
public interface LevelEquationVisitor {

	/** */
	public void visit(Level level);
	/** */
	public void visit(LevelMinEquation levelEquation);
	/** */
	public void visit(LevelMaxEquation levelEquation);
	
	/**
	 * 
	 * @author Thomas Vogel
	 * @version 0.1
	 */
	public static class LevelEquationValidityVistitor implements LevelEquationVisitor {
		
		/** */
		private SecurityAnnotation securityAnnotation = null;
		/** */
		private Set<String> containedLevels = new HashSet<String>();
		/** */
		private List<String> validLevels = new ArrayList<String>();
		/** */
		private LevelEquation levelEquation = null;
		/** */
		private boolean valid = true;
		
		/**
		 * 
		 * @param levelEquation
		 * @param securityAnnotation
		 */
		protected LevelEquationValidityVistitor(LevelEquation levelEquation, SecurityAnnotation securityAnnotation) {
			this.securityAnnotation = securityAnnotation;
			this.levelEquation = levelEquation;
		}

		/**
		 * 
		 * @param level
		 * @see security.LevelEquationVisitor#visit(security.LevelEquation.Level)
		 */
		@Override
		public void visit(Level level) {
			containedLevels.add(level.getLevel());
			boolean result = securityAnnotation.getAvailableLevels().contains(level.getLevel()) || level.getLevel().equals(SecurityAnnotation.VOID_LEVEL);
			if (! result) {
				valid = false;
			} else {
				validLevels.add(level.getLevel());
			}
		}

		/**
		 * 
		 * @param levelEquation
		 * @see security.LevelEquationVisitor#visit(security.LevelEquation.LevelMinEquation)
		 */
		@Override
		public void visit(LevelMinEquation levelEquation) {
			return;	
		}

		/**
		 * 
		 * @param levelEquation
		 * @see security.LevelEquationVisitor#visit(security.LevelEquation.LevelMaxEquation)
		 */
		@Override
		public void visit(LevelMaxEquation levelEquation) {
			return;			
		}
		
		/**
		 * 
		 * @return
		 */
		public List<String> getValidLevels() {
			return validLevels;
		}
		
		/**
		 * 
		 * @return
		 */
		public boolean isValid() {
			return valid;
		}
		
		/**
		 * 
		 * @return
		 */
		public boolean isVoidInvalid() {
			return containedLevels.size() > 1 && containedLevels.contains(SecurityAnnotation.VOID_LEVEL);
		}

		/**
		 * 
		 * @return
		 */
		public LevelEquation getLevelEquation() {
			return levelEquation;
		}		
	}
	
	/**
	 * 
	 * @author Thomas Vogel
	 * @version 0.1
	 */
	public static class LevelEquationCalculateVoidVisitor implements LevelEquationVisitor {
		
		/** */
		private Set<String> containedLevels = new HashSet<String>();
		
		/**
		 * 
		 */
		protected LevelEquationCalculateVoidVisitor() {
			super();
		}

		/**
		 * 
		 * @param level
		 * @see security.LevelEquationVisitor#visit(security.LevelEquation.Level)
		 */
		@Override
		public void visit(Level level) {
			containedLevels.add(level.getLevel());			
		}

		/**
		 * 
		 * @param levelEquation
		 * @see security.LevelEquationVisitor#visit(security.LevelEquation.LevelMinEquation)
		 */
		@Override
		public void visit(LevelMinEquation levelEquation) {
			return;			
		}

		/**
		 * 
		 * @param levelEquation
		 * @see security.LevelEquationVisitor#visit(security.LevelEquation.LevelMaxEquation)
		 */
		@Override
		public void visit(LevelMaxEquation levelEquation) {
			return;			
		}
		
		/**
		 * 
		 * @return
		 */
		public boolean isValid() {
			return containedLevels.size() == 1 && containedLevels.contains(SecurityAnnotation.VOID_LEVEL);
		}
		
	}
	
	/**
	 * 
	 * @author Thomas Vogel
	 * @version 0.1
	 */
	public static class LevelEquationEvaluationVisitor implements LevelEquationVisitor {
		
		/** */
		private Map<String, String> translateMap = new HashMap<String, String>();
		/** */
		private SecurityAnnotation securityAnnotation = null;
		/** */
		private List<String> resultingLevel = new ArrayList<String>();

		/**
		 * 
		 * @param argumentLevels
		 * @param parameterLevels
		 * @param securityAnnotation
		 */
		protected LevelEquationEvaluationVisitor(List<String> argumentLevels,
				List<String> parameterLevels, SecurityAnnotation securityAnnotation) {
			this.securityAnnotation = securityAnnotation;
			if (parameterLevels.size() == argumentLevels.size()) {
				for (int i = 0; i < parameterLevels.size(); i++) {
					String parameterLevel = parameterLevels.get(i);
					String argumentLevel = argumentLevels.get(i);
					if (parameterLevel.startsWith(SecurityAnnotation.LEVEL_PATTERN_SIGN)) {
						translateMap.put(parameterLevel, argumentLevel);
					}					
				}
			} else {
				System.out.println("INIT ERR");
				// TODO throw exception
			}
		}

		/**
		 * 
		 * @param level
		 * @see security.LevelEquationVisitor#visit(security.LevelEquation.Level)
		 */
		@Override
		public void visit(Level level) {
			String lev = level.getLevel();
			if (lev.startsWith(SecurityAnnotation.LEVEL_PATTERN_SIGN)) {
				String trans = translateMap.get(lev);
				resultingLevel.add(trans);
			} else {
				resultingLevel.add(lev);
			}		
		}

		/**
		 * 
		 * @param levelEquation
		 * @see security.LevelEquationVisitor#visit(security.LevelEquation.LevelMinEquation)
		 */
		@Override
		public void visit(LevelMinEquation levelEquation) {
			if (resultingLevel.size() == 2) {
				try {
					String result = securityAnnotation.getMinLevel(resultingLevel.get(0), resultingLevel.get(1));
					resultingLevel.clear();
					resultingLevel.add(result);
				} catch (Exception e) {
					System.out.println("MIN ERR");
				}
			} else {
				// TODO throw exception
				System.out.println("MIN ERR");
			}
		}

		/**
		 * 
		 * @param levelEquation
		 * @see security.LevelEquationVisitor#visit(security.LevelEquation.LevelMaxEquation)
		 */
		@Override
		public void visit(LevelMaxEquation levelEquation) {
			if (resultingLevel.size() == 2) {
				try {
					String result = securityAnnotation.getMaxLevel(resultingLevel.get(0), resultingLevel.get(1));
					resultingLevel.clear();
					resultingLevel.add(result);
				} catch (Exception e) {
					// TODO: handle exception
					System.out.println("MAX ERR");
				}
			} else {
				// TODO throw exception
				System.out.println("MAX ERR");
			}
			
		}
		
		/**
		 * 
		 * @return
		 */
		public String getResultLevel() {
			if (resultingLevel.size() == 1) {
				return resultingLevel.get(0);
			} else {
				// TODO throw exception
				System.out.println("NONE ERR");
				return null;
			}
		}
	}
}
