# Complete 3-Level Goal System

## 🎯 Overview

The Parva app now supports **goals at every level** of the hierarchy, allowing you to plan from the big picture down to daily intentions!

---

## 📊 Complete Goal Hierarchy

```
Maha-Parva (343 days)
├─ Title: "Be awesome in React"
├─ Description: "Master React in 343 days"
│
└─ 7 Parvas (49 days each)
    ├─ Parva 1: Beginning (Violet)
    │   ├─ Theme: "Foundation is laid" (automatic)
    │   ├─ ✨ Custom Goal: "Learn React Hooks" (YOU set)
    │   │
    │   └─ 7 Saptahas (7 days each)
    │       ├─ Saptaha 1: Beginning
    │       │   ├─ Theme: "Foundation" (automatic)
    │       │   ├─ ✨ Custom Goal: "Study useState & useEffect" (YOU set)
    │       │   │
    │       │   └─ 7 Dinas (1 day each)
    │       │       └─ Dina 1: Initiate
    │       │           ├─ Theme: "Start fresh" (automatic)
    │       │           ├─ ✨ Daily Intention: "Watch 2 hook tutorials" (YOU set)
    │       │           └─ 📝 Notes: "Learned useState..." (reflection)
```

---

## 🎨 Three Types of Goals

### 1. **Parva Goals** (49 days)
**Purpose:** What you want to achieve in this 7-week period

**Examples:**
- "Learn React Hooks and state management"
- "Build 3 projects with hooks"
- "Master component patterns"

**UI:** Card at top of Parva screen
**Editing:** Pencil icon (current/future only)
**Templates:** 5 suggestions based on theme

---

### 2. **Saptaha Goals** (7 days)
**Purpose:** What you want to accomplish this week

**Examples:**
- "Study useState and useEffect"
- "Build a todo app with hooks"
- "Debug hook issues in my project"

**UI:** Card at top of Saptaha (week) screen
**Editing:** Pencil icon (current/future only)
**Templates:** Same 5 suggestions (themes repeat!)

---

### 3. **Dina Intentions + Notes** (1 day)
**Two fields:**

#### 🎯 Daily Intention (What you PLAN)
- Set at start of day
- Specific actions for today
- Can't edit past days

**Examples:**
- "Watch React hooks tutorial on YouTube"
- "Complete 3 exercises on hooks"
- "Build useState example"

#### 📝 Notes (What you DID)
- Write at end of day
- Reflections and learnings
- Can edit anytime

**Examples:**
- "Completed tutorial. useState makes sense now!"
- "Built todo app. Struggled with useEffect cleanup"
- "Learned custom hooks pattern"

---

## ✨ Goal Templates

Each of the 7 themes provides 5 tailored prompts:

### Beginning (Violet)
- Learn the fundamentals of...
- Establish a foundation in...
- Start exploring...
- Build basic knowledge of...
- Get introduced to...

### Practice (Indigo)
- Practice daily with...
- Build consistency in...
- Repeat and reinforce...
- Develop muscle memory for...
- Do exercises on...

### Discernment (Blue)
- Analyze and evaluate...
- Identify patterns in...
- Understand what works in...
- Refine my approach to...
- Debug and troubleshoot...

### Ascent (Green)
- Push beyond basics in...
- Take on challenging...
- Accelerate growth in...
- Build advanced skills in...
- Go deeper into...

### Mastery (Yellow)
- Master advanced concepts in...
- Achieve fluency with...
- Become proficient in...
- Polish and perfect...
- Demonstrate expertise in...

### Flow (Orange)
- Work effortlessly with...
- Create projects using...
- Apply naturally in...
- Integrate into workflow...
- Build real applications with...

### Renewal (Red)
- Review and consolidate...
- Reflect on journey with...
- Document learnings about...
- Prepare for next phase of...
- Integrate and synthesize...

---

## 🔒 Editing Rules

### Parva & Saptaha Goals:
- ✅ **CAN edit:** Current and future periods
- ❌ **CANNOT edit:** Past periods (locked)
- **Why:** Preserve your journey

### Dina (Daily):
- ✅ **Intention:** Only today and future
- ✅ **Notes:** Anytime (even past days)
- **Why:** Intention is planning, notes are reflection

**Visual Indicator:** Pencil icon only shows when editable

---

## 🚀 Complete Workflow Example

### Scenario: Learning React in 343 days

#### Maha-Parva Setup
```
Title: "Become a React Expert"
Description: "Master React, hooks, state management, routing, and build real apps"
```

#### Parva 1: Beginning (Days 1-49)
```
Goal: "Learn React fundamentals - JSX, components, props, useState, useEffect"
```

##### Saptaha 1: Beginning (Days 1-7)
```
Goal: "Understand what React is and basic JSX syntax"
```

###### Dina 1: Initiate (Day 1)
```
Intention: "Watch 'React in 100 seconds' and 'React beginner tutorial part 1'"
Notes: (end of day) "Watched videos. React uses components and JSX. JSX looks like HTML but it's JavaScript!"
```

###### Dina 2: Stabilize (Day 2)
```
Intention: "Complete React official tutorial sections 1-3"
Notes: "Finished tic-tac-toe tutorial. Components make sense. Props are like function parameters!"
```

###### Dina 3: Observe (Day 3)
```
Intention: "Build a simple hello world app from scratch"
Notes: "Created first app! Learned about npm, create-react-app, and folder structure."
```

... and so on through 343 days!

---

## 🎨 UI Display

### Parva Screen (49 days)
```
┌─────────────────────────────────────────┐
│ ← Beginning Parva                 ☰ ⚙  │
├─────────────────────────────────────────┤
│                                         │
│ ┌─ My Goal for this Parva (49 days) ✏─┐│
│ │                                      ││
│ │ Learn React Hooks and state          ││
│ │ management fundamentals              ││
│ │                                      ││
│ └──────────────────────────────────────┘│
│                                         │
│         [Mandala or List View]          │
│                                         │
└─────────────────────────────────────────┘
```

### Saptaha Screen (7 days)
```
┌─────────────────────────────────────────┐
│ ← Beginning Saptaha               ←    │
├─────────────────────────────────────────┤
│ Saptaha 1: Beginning                    │
│ Foundation is laid...                   │
│ Jan 1 - Jan 7                           │
│                                         │
│ ┌─ My Goal for this Week ──────────✏─┐ │
│ │                                     ││
│ │ Study useState and useEffect        ││
│ │                                     ││
│ └─────────────────────────────────────┘│
│                                         │
│ Day 1: Initiate                         │
│ Day 2: Stabilize                        │
│ ...                                     │
└─────────────────────────────────────────┘
```

### Dina Screen (1 day)
```
┌─────────────────────────────────────────┐
│ ← Day 1                           ✓    │
├─────────────────────────────────────────┤
│ Day 1 of 343                            │
│ Initiate - Start fresh                  │
│ Monday, January 1, 2024                 │
│                                         │
│ ┌─ 🎯 Daily Intention ────────────────┐ │
│ │ What do you intend to accomplish?   ││
│ │                                     ││
│ │ [Watch 2 React hook tutorials___]  ││
│ │ [and complete setup___________]    ││
│ │                                     ││
│ └─────────────────────────────────────┘│
│                                         │
│ ┌─ 📝 Reflection & Notes ─────────────┐ │
│ │ Record what you did and learned     ││
│ │                                     ││
│ │ [Watched both tutorials. useState_] ││
│ │ [makes sense now! It's like regular]││
│ │ [variables but triggers re-render_] ││
│ │ [_____________________________]    ││
│ │                                     ││
│ └─────────────────────────────────────┘│
│                                         │
│        [────── Save ──────]             │
└─────────────────────────────────────────┘
```

---

## 💡 Best Practices

### 1. **Work Top-Down Initially**
First time:
1. Set Maha-Parva title/description
2. Set Parva 1 goal (49 days)
3. Set Saptaha 1 goal (this week)
4. Set Dina 1 intention (today)

### 2. **Plan 1-2 Weeks Ahead**
Don't plan all 343 days at once!
- Set current Parva goal
- Set current + next Saptaha goals
- Set daily intentions each morning

### 3. **Review and Adjust Weekly**
Every Sunday (or Dina 7 - Reflect):
- Review last week's progress
- Set next week's Saptaha goal
- Adjust Parva goal if needed

### 4. **Align Goals with Themes**
Use themes as guides:
- Beginning → Focus on fundamentals
- Practice → Focus on repetition
- Discernment → Focus on understanding
- Ascent → Focus on advanced topics
- Mastery → Focus on expertise
- Flow → Focus on creating
- Renewal → Focus on review

### 5. **Intention vs. Notes**
**Morning:** Set intention (what you'll do)  
**Evening:** Write notes (what you did + learned)

This creates a powerful before/after record!

---

## 🔥 Power User Tips

### Quick Planning Session
**Sunday evening:**
1. Review past week (read notes from 7 Dinas)
2. Set goal for upcoming week (Saptaha)
3. Rough plan for 7 days ahead

**Every morning:**
1. Set today's intention (5 minutes)
2. Review yesterday's notes

**Every evening:**
1. Write reflection notes (10 minutes)
2. Mark day complete

### Template Customization
When using templates:
1. Tap a template
2. Replace "..." with your specific topic
3. Add detail and context
4. Make it actionable

Example:
- Template: "Learn the fundamentals of..."
- Your goal: "Learn the fundamentals of React Hooks - specifically useState, useEffect, and custom hooks with 5 practice exercises"

---

## 📈 Tracking Progress

### At Each Level

**Maha-Parva (343 days):**
- Overall progress bar
- Which Parva you're in
- Days completed

**Parva (49 days):**
- Your custom goal displayed prominently
- 7 Saptahas shown
- Color-coded by theme

**Saptaha (7 days):**
- Your weekly goal displayed
- 7 Dinas listed
- Today highlighted

**Dina (1 day):**
- Intention at top
- Notes section below
- Completion toggle

---

## 🎊 Summary

You now have a **complete 3-level goal system**:

| Level | Duration | Purpose | Fields |
|-------|----------|---------|---------|
| **Maha-Parva** | 343 days | Main objective | Title + Description |
| **Parva** | 49 days | Phase goal | Custom Goal |
| **Saptaha** | 7 days | Weekly goal | Custom Goal |
| **Dina** | 1 day | Daily plan + reflection | Intention + Notes |

**Benefits:**
- ✅ Plan at every level of detail
- ✅ Automatic themes provide structure
- ✅ Custom goals provide specificity
- ✅ Templates provide inspiration
- ✅ Past goals are locked to preserve history
- ✅ Intention/notes separation for before/after

**Your journey, your goals, at every scale!** 🪷✨

---

**Version:** 2.0.0  
**Feature:** Complete 3-Level Goal System  
**Status:** ✅ Ready to Use

