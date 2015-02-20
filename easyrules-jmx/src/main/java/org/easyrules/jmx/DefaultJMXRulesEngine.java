package org.easyrules.jmx;

import java.util.TreeSet;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.easyrules.api.Rule;
import org.easyrules.core.AbstractRulesEngine;
import org.easyrules.jmx.api.JMXRule;
import org.easyrules.jmx.api.JMXRulesEngine;
import org.easyrules.jmx.util.MBeanManager;
import org.easyrules.util.EasyRulesConstants;

/**
 * Default {@link org.easyrules.jmx.api.JMXRulesEngine} implementation.
 *
 * This implementation handles a set of JMX rules with unique name. All other
 * behavior 
 * 
 * @author Drem Darios (drem.darios@gmail.com)
 */
public class DefaultJMXRulesEngine extends AbstractRulesEngine<JMXRule>
		implements JMXRulesEngine<JMXRule> {

	private static final Logger LOGGER = Logger
			.getLogger(EasyRulesConstants.LOGGER_NAME);

	private MBeanManager beanManager = new MBeanManager();
	
    /**
     * Construct a default rules engine with default values.
     */
    public DefaultJMXRulesEngine() {
        this(false, EasyRulesConstants.DEFAULT_RULE_PRIORITY_THRESHOLD);
    }

    /**
     * Constructs a default rules engine.
     * @param skipOnFirstAppliedRule true if the engine should skip next rule after the first applied rule
     */
    public DefaultJMXRulesEngine(boolean skipOnFirstAppliedRule) {
        this(skipOnFirstAppliedRule, EasyRulesConstants.DEFAULT_RULE_PRIORITY_THRESHOLD);
    }

    /**
     * Constructs a default rules engine.
     * @param rulePriorityThreshold rule priority threshold
     */
    public DefaultJMXRulesEngine(int rulePriorityThreshold) {
        this(false, rulePriorityThreshold);
    }

    /**
     * Constructs a default rules engine.
     * @param skipOnFirstAppliedRule true if the engine should skip next rule after the first applied rule
     * @param rulePriorityThreshold rule priority threshold
     */
    public DefaultJMXRulesEngine(boolean skipOnFirstAppliedRule, int rulePriorityThreshold) {
        rules = new TreeSet<JMXRule>();
        this.skipOnFirstAppliedRule = skipOnFirstAppliedRule;
        this.rulePriorityThreshold = rulePriorityThreshold;
    }
    
	@Override
	public void registerJMXRule(JMXRule rule) {
		registerRule(rule);
		beanManager.registerJmxMBean(rule);
	}

	@Override
	public void unregisterJMXRule(JMXRule rule) {
		unregisterRule(rule);
		beanManager.unregisterJmxMBean(rule);
	}
	
	@Override
    public void fireRules() {

        if (rules.isEmpty()) {
            LOGGER.warning("No rules registered! Nothing to apply.");
            return;
        }

        //resort rules in case priorities were modified via JMX
        rules = new TreeSet<JMXRule>(rules);

        for (Rule rule : rules) {

            if (rule.getPriority() > rulePriorityThreshold) {
                LOGGER.log(Level.INFO, "Rule priority threshold {0} exceeded at rule {1} (priority={2}), next applicable rules will be skipped.",
                        new Object[] {rulePriorityThreshold, rule.getName(), rule.getPriority()});
                break;
            }

            if (rule.evaluateConditions()) {
                LOGGER.log(Level.INFO, "Rule {0} triggered.", new Object[]{rule.getName()});
                try {
                    rule.performActions();
                    LOGGER.log(Level.INFO, "Rule {0} performed successfully.", new Object[]{rule.getName()});
                    if (skipOnFirstAppliedRule) {
                        LOGGER.info("Next rules will be skipped according to parameter skipOnFirstAppliedRule.");
                        break;
                    }
                } catch (Exception exception) {
                    LOGGER.log(Level.SEVERE, "Rule '" + rule.getName() + "' performed with error.", exception);
                }
            }

        }

    }
	
}
