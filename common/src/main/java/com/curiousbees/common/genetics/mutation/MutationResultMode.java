package com.curiousbees.common.genetics.mutation;

public enum MutationResultMode {
    /** Replaces only the active species allele; the inactive allele is preserved. */
    PARTIAL,
    /** Replaces both species alleles with the mutation result. */
    FULL
}
